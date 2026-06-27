package com.stocknegoce.wms.repository.global;

import java.util.List;

import com.stocknegoce.wms.model.PurchaseOrder;
import com.stocknegoce.wms.repository.GenericRepository;

public interface PurchaseOrderRepository extends GenericRepository<PurchaseOrder, Integer> {

    List<PurchaseOrder> findByStatusOrderByOrderedatDesc(String status);
    List<PurchaseOrder> findAllByOrderByOrderedatDesc();
}
