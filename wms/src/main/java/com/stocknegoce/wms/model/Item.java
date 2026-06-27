package com.stocknegoce.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer idItem;

    @Column(name = "barcode", unique = true, length = 20)
    private String barCode;

    @Column(unique = true, nullable = false, length = 30)
    private String reference;

    @Column(nullable = false, length = 150)
    private String label;

    @Column(length = 50)
    private String family;

    @Column(nullable = false, length = 20)
    private String unit;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier preferredSupplier;

    @Column(name = "abc_class", length = 1)
    private String abcClass;

    @Column(name = "abc_updated_at")
    private java.sql.Date abcUpdatedAt;

    @Column(name = "order_threshold")
    private Float orderThreshold = 0f;

    @Column(name = "safety_stock")
    private Float safetyStock = 0f;

    @Column(name = "alert_threshold")
    private Float alertThreshold = 0f;

    @Column(name = "safety_margin")
    private Float safetyMargin = 0f;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private java.sql.Timestamp createdat;
}
