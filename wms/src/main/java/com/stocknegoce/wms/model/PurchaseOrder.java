package com.stocknegoce.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "purchase_order")
@Getter
@Setter
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_supplier")
    private Supplier supplier;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(nullable = false, length = 20)
    private String status; // EN_ATTENTE, RECUE, ANNULEE

    @Column(length = 500)
    private String note;

    @Column(nullable = false)
    private java.sql.Timestamp orderedat;

    private java.sql.Timestamp receivedat;

    @Column(name = "real_delay_d")
    private Float realDelayD;
}