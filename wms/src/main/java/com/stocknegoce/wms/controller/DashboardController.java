package com.stocknegoce.wms.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.model.Movement;
import com.stocknegoce.wms.model.StockLine;
import com.stocknegoce.wms.repository.global.MovementRepository;
import com.stocknegoce.wms.repository.global.StockLineRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final MovementRepository movementRepository;
    private final StockLineRepository stockLineRepository;

    public DashboardController(MovementRepository movementRepository, StockLineRepository stockLineRepository) {
        this.movementRepository = movementRepository;
        this.stockLineRepository = stockLineRepository;
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<?> getDashboard() {
        // Calcul début et fin de journée
        LocalDate today = LocalDate.now();
        Timestamp start = Timestamp.valueOf(today.atStartOfDay());
        Timestamp end = Timestamp.valueOf(today.plusDays(1).atStartOfDay());

        // Mouvements du jour
        Long todayCount = movementRepository.countMovementsBetween(start, end);
        List<Movement> todayMovements = movementRepository.findMovementsBetween(start, end);

        // 5 derniers mouvements
        List<Movement> lastMovements = movementRepository.findTop5ByOrderByCreatedAtDesc();

        // Stock bas
        List<StockLine> lowStock = stockLineRepository.findLowStock();

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("todayCount", todayCount);
        dashboard.put("todayMovements", todayMovements);
        dashboard.put("lastMovements", lastMovements);
        dashboard.put("lowStockCount", lowStock.size());
        dashboard.put("lowStock", lowStock);

        return ResponseEntity.ok(dashboard);
    }
}