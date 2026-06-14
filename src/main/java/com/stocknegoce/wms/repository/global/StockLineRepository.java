package com.stocknegoce.wms.repository.global;

import java.util.List;

import com.stocknegoce.wms.model.Location;
import com.stocknegoce.wms.model.StockLine;
import com.stocknegoce.wms.repository.GenericRepository;

public interface StockLineRepository extends GenericRepository<StockLine, Integer> {

    List<StockLine> findByLocation(Location location);
}
