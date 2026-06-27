package com.stocknegoce.wms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alert")
@Getter
@Setter
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_alert;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "acknowledged_by")
    private User acknowledgedBy;

    @Column(nullable = false, length = 30)
    private String type; // SEUIL_COMMANDE, RUPTURE, INVENTAIRE_DU, FIABILITE_FOURN

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private boolean acknowledged = false;

    private java.sql.Timestamp acknowledgedat;

    @Column(nullable = false)
    private java.sql.Timestamp createdat;
}