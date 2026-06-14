package com.stocknegoce.wms.controller.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.Location;
import com.stocknegoce.wms.model.StockLine;
import com.stocknegoce.wms.repository.global.LocationRepository;
import com.stocknegoce.wms.repository.global.StockLineRepository;

@RestController
@RequestMapping("/api/locations")
public class LocationController extends GenericController<Location, Integer> {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private StockLineRepository stockLineRepository;

    public LocationController(LocationRepository locationRepository, StockLineRepository stockLineRepository) {
        super(locationRepository);
        this.locationRepository = locationRepository;
        this.stockLineRepository = stockLineRepository;
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Location location) {
        Optional<Location> locationOptional = locationRepository.findById(id);

        if (locationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Emplacement introuvable");
        }

        location.setId_location(id);
        Location updatedLocation = locationRepository.save(location);
        return ResponseEntity.ok(updatedLocation);
    }

    @GetMapping("/scan/{qrToken}")
    @CrossOrigin
    public ResponseEntity<?> scan(@PathVariable("qrToken") String qrToken) {
        Optional<Location> locationOptional = locationRepository.findByQrToken(qrToken);

        if (locationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Emplacement introuvable pour ce QR code");
        }

        Location location = locationOptional.get();
        List<StockLine> stockLines = stockLineRepository.findByLocation(location);

        Map<String, Object> response = new HashMap<>();
        response.put("location", location);
        response.put("stock", stockLines);

        return ResponseEntity.ok(response);
    }
}