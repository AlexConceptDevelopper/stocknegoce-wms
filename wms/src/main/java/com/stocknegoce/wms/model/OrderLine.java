package com.stocknegoce.wms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_line")
@Getter
@Setter
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_order_line;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_order")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_item")
    private Item item;

    @Column(name = "quantity_ordered", nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal quantityOrdered;

    @Column(name = "quantity_received", precision = 15, scale = 2)
    private java.math.BigDecimal quantityReceived;

    @Column(length = 500)
    private String note;
}
