package com.stocknegoce.wms.controller.global;

import java.util.List;
import java.util.Optional;

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
import com.stocknegoce.wms.model.OrderLine;
import com.stocknegoce.wms.model.PurchaseOrder;
import com.stocknegoce.wms.repository.global.OrderLineRepository;
import com.stocknegoce.wms.repository.global.PurchaseOrderRepository;

@RestController
@RequestMapping("/api/orderlines")
public class OrderLineController extends GenericController<OrderLine, Integer> {

    private final OrderLineRepository orderLineRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public OrderLineController(OrderLineRepository orderLineRepository,
            PurchaseOrderRepository purchaseOrderRepository) {
        super(orderLineRepository);
        this.orderLineRepository = orderLineRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * Toutes les lignes d'une commande
     * GET /api/orderlines/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    @CrossOrigin
    public ResponseEntity<?> getByOrder(@PathVariable Integer orderId) {
        if (!purchaseOrderRepository.existsById(orderId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande introuvable");
        }
        return ResponseEntity.ok(orderLineRepository.findByOrder(orderId));
    }

    @GetMapping("/order/{orderId}/pending")
    @CrossOrigin
    public ResponseEntity<?> getPendingByOrder(@PathVariable Integer orderId) {
        if (!purchaseOrderRepository.existsById(orderId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande introuvable");
        }
        return ResponseEntity.ok(orderLineRepository.findPendingByOrder(orderId));
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody OrderLine orderLine) {
        Optional<OrderLine> opt = orderLineRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ligne introuvable");
        }
        orderLine.setId_order_line(id);
        return ResponseEntity.ok(orderLineRepository.save(orderLine));
    }
}