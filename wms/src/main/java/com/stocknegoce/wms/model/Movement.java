package com.stocknegoce.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movement")
@Getter
@Setter
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_movement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_stockline")
    private StockLine stockLine;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal quantity;

    @Column(nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal quantityBefore;

    @Column(nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal quantityAfter;

    @Column(length = 100)
    private String clientName;

    @Column(length = 500)
    private String note;

    @Column(nullable = false)
    private java.sql.Timestamp createdAt;
}