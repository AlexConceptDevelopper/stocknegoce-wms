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
    private Integer id_item;

    @Column(unique = true, length = 20)
    private String barCode;

    @Column(unique = true, nullable = false, length = 30)
    private String reference;

    @Column(nullable = false, length = 150)
    private String label;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(nullable = false)
    private java.sql.Timestamp createdAt;
}
