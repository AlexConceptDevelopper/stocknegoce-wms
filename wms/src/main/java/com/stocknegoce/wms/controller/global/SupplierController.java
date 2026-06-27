package com.stocknegoce.wms.controller.global;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.Supplier;
import com.stocknegoce.wms.repository.global.SupplierRepository;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController extends GenericController<Supplier, Integer> {

    @Autowired
    private SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        super(supplierRepository);
        this.supplierRepository = supplierRepository;
    }

    @GetMapping("/active")
    @CrossOrigin
    public ResponseEntity<?> getActive() {
        return ResponseEntity.ok(supplierRepository.findByActiveTrue());
    }

    @GetMapping("/type/{type}")
    @CrossOrigin
    public ResponseEntity<?> getByType(@PathVariable String type) {
        return ResponseEntity.ok(supplierRepository.findByTypeOrderByNameAsc(type));
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Supplier supplier) {
        Optional<Supplier> opt = supplierRepository.findById(id);
        if (opt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fournisseur introuvable");
        supplier.setId_supplier(id);
        return ResponseEntity.ok(supplierRepository.save(supplier));
    }
}
