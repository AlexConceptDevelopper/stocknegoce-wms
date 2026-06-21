package com.stocknegoce.wms.controller.global;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.Movement;
import com.stocknegoce.wms.model.DTO.MovementRequest;
import com.stocknegoce.wms.repository.global.MovementRepository;
import com.stocknegoce.wms.service.StockService;

@RestController
@RequestMapping("/api/movements")
public class MovementController extends GenericController<Movement, Integer> {

    private final MovementRepository movementRepository;
    private final StockService stockService;

    public MovementController(MovementRepository movementRepository, StockService stockService) {
        super(movementRepository);
        this.movementRepository = movementRepository;
        this.stockService = stockService;
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Movement movement) {
        Optional<Movement> movementOptional = movementRepository.findById(id);

        if (movementOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mouvement introuvable");
        }

        movement.setId_movement(id);
        Movement updatedMovement = movementRepository.save(movement);
        return ResponseEntity.ok(updatedMovement);
    }

    /**
     * Endpoint principal — traite un mouvement de stock.
     * Reçoit un MovementRequest JSON, délègue la logique métier au StockService.
     * La transaction est gérée dans le service — si ça plante, tout est annulé.
     */
    @PostMapping("/process")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin
    public ResponseEntity<?> process(@RequestBody MovementRequest request) {
        try {
            Movement movement = stockService.processMovement(
                    request.getStockLineId(),
                    request.getType(),
                    request.getQuantity(),
                    request.getUserId(),
                    request.getClientName(),
                    request.getNote()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(movement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}