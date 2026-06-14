package com.stocknegoce.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "location")
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_location;

    @Column(nullable = false, length = 3)
    private String rack;

    @Column(nullable = false, length = 1)
    private String aisle;

    @Column(nullable = false)
    private Integer level;

    @Column(name = "column_", nullable = false)
    private Integer column;

    @Column(unique = true, nullable = false, length = 8)
    private String code;

    @Column(unique = true, nullable = false, length = 36)
    private String qrToken;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private java.sql.Timestamp createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_depot")
    private Depot depot;
}