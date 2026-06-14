package com.stocknegoce.wms.controller.global;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.Movement;
import com.stocknegoce.wms.repository.global.MovementRepository;

@RestController
@RequestMapping("/api/movements")
public class MovementController extends GenericController<Movement, Integer> {

    @Autowired
    private MovementRepository movementRepository;

    public MovementController(MovementRepository movementRepository) {
        super(movementRepository);
        this.movementRepository = movementRepository;
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
}
