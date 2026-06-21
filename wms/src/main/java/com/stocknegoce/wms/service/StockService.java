package com.stocknegoce.wms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stocknegoce.wms.model.Movement;
import com.stocknegoce.wms.model.StockLine;
import com.stocknegoce.wms.model.User;
import com.stocknegoce.wms.repository.global.MovementRepository;
import com.stocknegoce.wms.repository.global.StockLineRepository;

/**
 * Service gérant la logique métier des mouvements de stock.
 * Toute opération sur le stock (entrée, sortie, casse...) passe par ici.
 */
@Service
public class StockService {

    private final StockLineRepository stockLineRepository;
    private final MovementRepository movementRepository;

    /**
     * Injection des repositories via le constructeur (bonne pratique Spring)
     */
    public StockService(StockLineRepository stockLineRepository, MovementRepository movementRepository) {
        this.stockLineRepository = stockLineRepository;
        this.movementRepository = movementRepository;
    }

    /**
     * Traite un mouvement de stock (entrée ou sortie).
     * @Transactional garantit que si une étape plante, tout est annulé en base.
     *
     * @param stockLineId  id de la ligne de stock concernée
     * @param type         type de mouvement (ENTREE, SORTIE, CASSE, RETOUR_CLIENT...)
     * @param quantity     quantité du mouvement (toujours positive)
     * @param userId       id de l'utilisateur qui réalise l'opération
     * @param clientName   nom du client (renseigné pour les sorties)
     * @param note         commentaire libre (n° BL, motif...)
     * @return             le Movement créé et sauvegardé en base
     */
    @Transactional
    public Movement processMovement(
            Integer stockLineId,
            String type,
            java.math.BigDecimal quantity,
            Integer userId,
            String clientName,
            String note) {

        // ── Étape 1 : Récupérer la ligne de stock concernée ──────────────────
        // Si elle n'existe pas, on lance une exception qui annule toute la transaction
        StockLine stockLine = stockLineRepository.findById(stockLineId)
                .orElseThrow(() -> new RuntimeException("StockLine introuvable : " + stockLineId));

        // ── Étape 2 : Vérifier que le stock est suffisant pour une sortie ─────
        // Inutile de vérifier pour une entrée — on peut toujours en rajouter
        if (type.equals("SORTIE") || type.equals("CASSE") || type.equals("DECLASSE")) {
            if (stockLine.getQuantity().compareTo(quantity) < 0) {
                throw new RuntimeException("Stock insuffisant : " + stockLine.getQuantity() + " disponible(s)");
            }
        }

        // ── Étape 3 : Calculer les quantités avant et après le mouvement ──────
        java.math.BigDecimal quantityBefore = stockLine.getQuantity();
        java.math.BigDecimal quantityAfter;

        // Les entrées ajoutent au stock, les sorties retirent
        if (type.equals("ENTREE") || type.equals("RETOUR_CLIENT") || type.equals("NAVETTE_ENTREE")) {
            quantityAfter = quantityBefore.add(quantity);
        } else {
            quantityAfter = quantityBefore.subtract(quantity);
        }

        // ── Étape 4 : Mettre à jour la quantité dans StockLine ───────────────
        // C'est la photographie actuelle du stock — on la met à jour immédiatement
        stockLine.setQuantity(quantityAfter);
        stockLine.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        stockLineRepository.save(stockLine);

        // ── Étape 5 : Créer le mouvement pour garder la trace de l'opération ─
        // Le mouvement est immuable — on ne le modifiera jamais, c'est l'historique
        Movement movement = new Movement();
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setQuantityBefore(quantityBefore);
        movement.setQuantityAfter(quantityAfter);
        movement.setClientName(clientName);
        movement.setNote(note);
        movement.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        movement.setStockLine(stockLine);

        // ── Étape 6 : Lier l'utilisateur qui a réalisé l'opération ──────────
        // On crée une référence légère (juste l'id) sans recharger tout l'objet User
        User user = new User();
        user.setId_user(userId);
        movement.setUser(user);

        // ── Étape 7 : Sauvegarder et retourner le mouvement créé ─────────────
        return movementRepository.save(movement);
    }
}