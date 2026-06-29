package com.stocknegoce.wms.repository.global;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stocknegoce.wms.model.OrderLine;
import com.stocknegoce.wms.repository.GenericRepository;

public interface OrderLineRepository extends GenericRepository<OrderLine, Integer> {

    /**
     * Toutes les lignes d'une commande par id
     */
    @Query("SELECT ol FROM OrderLine ol WHERE ol.purchaseOrder.id_order = :orderId ORDER BY ol.id_order_line ASC")
    List<OrderLine> findByOrder(@Param("orderId") Integer orderId);

    /**
     * Lignes non encore réceptionnées
     */
    @Query("SELECT ol FROM OrderLine ol WHERE ol.purchaseOrder.id_order = :orderId AND ol.quantityReceived IS NULL")
    List<OrderLine> findPendingByOrder(@Param("orderId") Integer orderId);
}