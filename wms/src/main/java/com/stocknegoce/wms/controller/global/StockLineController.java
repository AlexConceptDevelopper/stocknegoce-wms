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
import com.stocknegoce.wms.model.StockLine;
import com.stocknegoce.wms.repository.global.StockLineRepository;

@RestController
@RequestMapping("/api/stocklines")
public class StockLineController extends GenericController<StockLine, Integer> {

    @Autowired
    private StockLineRepository stockLineRepository;

    public StockLineController(StockLineRepository stockLineRepository) {
        super(stockLineRepository);
        this.stockLineRepository = stockLineRepository;
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody StockLine stockLine) {
        Optional<StockLine> stockLineOptional = stockLineRepository.findById(id);

        if (stockLineOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ligne de stock introuvable");
        }

        stockLine.setId_stockline(id);
        StockLine updatedStockLine = stockLineRepository.save(stockLine);
        return ResponseEntity.ok(updatedStockLine);
    }
}