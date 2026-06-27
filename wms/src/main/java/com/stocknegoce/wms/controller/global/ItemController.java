package com.stocknegoce.wms.controller.global;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.Item;
import com.stocknegoce.wms.repository.global.ItemRepository;

@RestController
@RequestMapping("/api/items")
public class ItemController extends GenericController<Item, Integer> {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        super(itemRepository);
        this.itemRepository = itemRepository;
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Item item) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article introuvable");
        }

        item.setIdItem(id);
        Item updatedItem = itemRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Recherche au comptoir — par désignation ou référence.
     * Ex : GET /api/items/search?q=parpaing
     * Retourne tous les articles dont le label OU la référence contient le terme recherché.
     */
    @GetMapping("/search")
    @CrossOrigin
    public ResponseEntity<?> search(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Terme de recherche vide");
        }

        // Recherche dans le label ET dans la référence
        List<Item> byLabel = itemRepository.findByLabelContainingIgnoreCase(query);
        List<Item> byReference = itemRepository.findByReferenceContainingIgnoreCase(query);

        // Fusionner les résultats sans doublons
        byLabel.addAll(byReference.stream()
                .filter(item -> byLabel.stream()
                        .noneMatch(i -> i.getIdItem().equals(item.getIdItem())))
                .toList());

        return ResponseEntity.ok(byLabel);
    }
}