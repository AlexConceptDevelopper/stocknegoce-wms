package com.stocknegoce.wms.model.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO représentant la requête entrante pour un mouvement de stock.
 * C'est l'objet que l'appli mobile envoie en JSON lors d'une entrée ou sortie.
 * On ne passe jamais l'entité Movement directement pour éviter les données parasites.
 */
@Getter
@Setter
public class MovementRequest {

    /** Id de la ligne de stock concernée (emplacement déjà connu via scan QR) */
    private Integer stockLineId;

    /** Type : ENTREE, SORTIE, CASSE, DECLASSE, RETOUR_CLIENT, NAVETTE_ENTREE */
    private String type;

    /** Quantité — toujours positive, le type porte le sens */
    private BigDecimal quantity;

    /** Id de l'utilisateur connecté qui réalise l'opération */
    private Integer userId;

    /** Nom du client — renseigné uniquement pour les sorties */
    private String clientName;

    /** Commentaire libre — n° BL, motif, remarque... */
    private String note;
}