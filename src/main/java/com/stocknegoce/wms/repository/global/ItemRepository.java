package com.stocknegoce.wms.repository.global;

import java.util.List;

import com.stocknegoce.wms.model.Item;
import com.stocknegoce.wms.repository.GenericRepository;

public interface ItemRepository extends GenericRepository<Item, Integer> {

    /**
     * Recherche par désignation (label) — insensible à la casse.
     * Ex : "parpaing" trouve "Parpaing 20 plein", "Parpaing creux 15"...
     */
    List<Item> findByLabelContainingIgnoreCase(String label);

    /**
     * Recherche par référence exacte.
     * Ex : "PAR-20-PLEIN" trouve l'article précis.
     */
    List<Item> findByReferenceContainingIgnoreCase(String reference);
}
