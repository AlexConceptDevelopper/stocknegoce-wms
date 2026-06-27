package com.stocknegoce.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "supplier")
@Getter
@Setter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_supplier;

    @ManyToOne
    @JoinColumn(name = "id_depot_source")
    private Depot depotSource;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(length = 100)
    private String contact;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(name = "delay_min_d")
    private Short delayMinD;

    @Column(name = "delay_max_d")
    private Short delayMaxD;

    @Column(name = "delay_median_calc")
    private Double delayMedianCalc;

    @Column(name = "delay_worst_calc")
    private Double delayWorstCalc;

    @Column(name = "reliability_pct")
    private Double reliabilityPct;

    @Column(name = "nb_deliveries")
    private Integer nbDeliveries = 0;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private java.sql.Timestamp createdat;
}