package com.stocknegoce.wms.repository.global;

import java.util.List;

import com.stocknegoce.wms.model.Alert;
import com.stocknegoce.wms.repository.GenericRepository;

public interface AlertRepository extends GenericRepository<Alert, Integer> {

    List<Alert> findByAcknowledgedFalseOrderByCreatedatDesc();

    List<Alert> findByItem_IdItemOrderByCreatedatDesc(Integer itemId);

    boolean existsByItem_IdItemAndTypeAndAcknowledgedFalse(
        Integer itemId,
        String type
    );
}
