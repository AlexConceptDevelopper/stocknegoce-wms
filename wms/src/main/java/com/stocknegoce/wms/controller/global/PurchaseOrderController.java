package com.stocknegoce.wms.controller.global;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.PurchaseOrder;
import com.stocknegoce.wms.model.Supplier;
import com.stocknegoce.wms.repository.global.PurchaseOrderRepository;
import com.stocknegoce.wms.repository.global.SupplierRepository;

@RestController
@RequestMapping("/api/orders")
public class PurchaseOrderController extends GenericController<PurchaseOrder, Integer> {

    @Autowired
    private PurchaseOrderRepository orderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public PurchaseOrderController(PurchaseOrderRepository orderRepository, SupplierRepository supplierRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping("/pending")
    @CrossOrigin
    public ResponseEntity<?> getPending() {
        return ResponseEntity.ok(orderRepository.findByStatusOrderByOrderedatDesc("EN_ATTENTE"));
    }

    @GetMapping("/all")
    @CrossOrigin
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(orderRepository.findAllByOrderByOrderedatDesc());
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody PurchaseOrder order) {
        Optional<PurchaseOrder> opt = orderRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande introuvable");
        order.setId_order(id);
        return ResponseEntity.ok(orderRepository.save(order));
    }

    /**
     * Réceptionner une commande — met à jour le statut, calcule le délai réel
     * et met à jour les stats du fournisseur
     */
    @PostMapping("/{id}/receive")
    @CrossOrigin
    public ResponseEntity<?> receive(@PathVariable Integer id, @RequestBody(required = false) String note) {
        Optional<PurchaseOrder> opt = orderRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande introuvable");

        PurchaseOrder order = opt.get();

        if (!order.getStatus().equals("EN_ATTENTE")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Commande déjà réceptionnée ou annulée");
        }

        // Calculer le délai réel en jours
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long diffMs = now.getTime() - order.getOrderedat().getTime();
        float realDelayDays = diffMs / (1000f * 60 * 60 * 24);

        order.setReceivedat(now);
        order.setStatus("RECUE");
        order.setRealDelayD(realDelayDays);
        if (note != null) order.setNote(note);

        orderRepository.save(order);

        // Mettre à jour les stats du fournisseur
        Supplier supplier = order.getSupplier();
        supplier.setNbDeliveries(supplier.getNbDeliveries() + 1);

        // Recalculer médiane et pire cas (simplifié — affiné plus tard avec l'historique complet)
        if (supplier.getDelayMedianCalc() == null) {
            supplier.setDelayMedianCalc((double) realDelayDays);
            supplier.setDelayWorstCalc((double) realDelayDays);
        } else {
            // Moyenne glissante simple pour la médiane
            double newMedian = (supplier.getDelayMedianCalc() * (supplier.getNbDeliveries() - 1) + realDelayDays) / supplier.getNbDeliveries();
            supplier.setDelayMedianCalc(newMedian);
            // Pire cas = max observé
            if (realDelayDays > supplier.getDelayWorstCalc()) {
                supplier.setDelayWorstCalc((double) realDelayDays);
            }
        }

        supplierRepository.save(supplier);

        return ResponseEntity.ok(order);
    }
}