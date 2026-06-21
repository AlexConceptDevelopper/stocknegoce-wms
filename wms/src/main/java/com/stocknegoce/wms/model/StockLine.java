package com.stocknegoce.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stockline")
@Getter
@Setter
public class StockLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_stockline;

    @Column(nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal quantity;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private java.sql.Timestamp updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_item")
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_location")
    private Location location;
}