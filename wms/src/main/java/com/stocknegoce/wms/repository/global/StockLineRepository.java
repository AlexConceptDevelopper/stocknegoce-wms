package com.stocknegoce.wms.repository.global;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.stocknegoce.wms.model.Location;
import com.stocknegoce.wms.model.StockLine;
import com.stocknegoce.wms.repository.GenericRepository;

public interface StockLineRepository extends GenericRepository<StockLine, Integer> {

    List<StockLine> findByLocation(Location location);

    /**
     * Articles dont la quantité est inférieure à 20 — stock bas
     * On affinera avec le vrai seuil par article en Phase 2
     */
    @Query("SELECT s FROM StockLine s WHERE s.quantity < 20 AND s.status = 'NORMAL'")
    List<StockLine> findLowStock();
}
