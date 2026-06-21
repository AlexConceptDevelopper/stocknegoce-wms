package com.stocknegoce.wms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.model.Movement;
import com.stocknegoce.wms.repository.global.MovementRepository;

/**
 * Controller dédié à l'historique des mouvements.
 * Permet de filtrer par article, emplacement, type ou nom client.
 */
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final MovementRepository movementRepository;

    public HistoryController(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    /**
     * GET /api/history — historique complet ou filtré
     * Paramètres optionnels :
     * - itemId : filtrer par article
     * - locationId : filtrer par emplacement
     * - type : filtrer par type (ENTREE, SORTIE, CASSE, RETOUR_CLIENT)
     * - client : filtrer par nom client
     */
    @GetMapping
    @CrossOrigin
    public ResponseEntity<?> getHistory(
            @RequestParam(required = false) Integer itemId,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String client) {

        List<Movement> movements;

        if (itemId != null) {
            movements = movementRepository.findByItemId(itemId);
        } else if (locationId != null) {
            movements = movementRepository.findByLocationId(locationId);
        } else if (type != null) {
            movements = movementRepository.findByTypeOrderByCreatedAtDesc(type);
        } else if (client != null) {
            movements = movementRepository.findByClientNameContainingIgnoreCaseOrderByCreatedAtDesc(client);
        } else {
            movements = movementRepository.findAllByOrderByCreatedAtDesc();
        }

        return ResponseEntity.ok(movements);
    }
}