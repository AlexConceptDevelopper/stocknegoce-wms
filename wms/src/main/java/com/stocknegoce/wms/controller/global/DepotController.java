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
import com.stocknegoce.wms.model.Depot;
import com.stocknegoce.wms.repository.global.DepotRepository;

@RestController
@RequestMapping("/api/depots")
public class DepotController extends GenericController<Depot, Integer> {

    @Autowired
    private DepotRepository depotRepository;

    public DepotController(DepotRepository depotRepository) {
        super(depotRepository);
        this.depotRepository = depotRepository;
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Depot depot) {
        Optional<Depot> depotOptional = depotRepository.findById(id);

        if (depotOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dépôt introuvable");
        }

        depot.setId_depot(id);
        Depot updatedDepot = depotRepository.save(depot);
        return ResponseEntity.ok(updatedDepot);
    }
}