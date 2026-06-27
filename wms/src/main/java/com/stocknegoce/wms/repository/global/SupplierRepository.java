package com.stocknegoce.wms.repository.global;

import com.stocknegoce.wms.model.Supplier;
import com.stocknegoce.wms.repository.GenericRepository;

public interface SupplierRepository extends GenericRepository<Supplier, Integer> {
    
    java.util.List<Supplier> findByTypeOrderByNameAsc(String type);
    java.util.List<Supplier> findByActiveTrue();
}