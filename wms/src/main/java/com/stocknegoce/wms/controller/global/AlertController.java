package com.stocknegoce.wms.controller.global;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.controller.GenericController;
import com.stocknegoce.wms.model.Alert;
import com.stocknegoce.wms.model.User;
import com.stocknegoce.wms.repository.global.AlertRepository;

@RestController
@RequestMapping("/api/alerts")
public class AlertController extends GenericController<Alert, Integer> {

    @Autowired
    private AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        super(alertRepository);
        this.alertRepository = alertRepository;
    }

    @GetMapping("/active")
    @CrossOrigin
    public ResponseEntity<?> getActive() {
        return ResponseEntity.ok(alertRepository.findByAcknowledgedFalseOrderByCreatedatDesc());
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Alert alert) {
        Optional<Alert> opt = alertRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alerte introuvable");
        alert.setId_alert(id);
        return ResponseEntity.ok(alertRepository.save(alert));
    }

    /**
     * Acquitter une alerte — marque comme traitée par un utilisateur
     */
    @PostMapping("/{id}/acknowledge/{userId}")
    @CrossOrigin
    public ResponseEntity<?> acknowledge(@PathVariable Integer id, @PathVariable Integer userId) {
        Optional<Alert> opt = alertRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alerte introuvable");

        Alert alert = opt.get();
        alert.setAcknowledged(true);
        alert.setAcknowledgedat(new Timestamp(System.currentTimeMillis()));

        User user = new User();
        user.setId_user(userId);
        alert.setAcknowledgedBy(user);

        return ResponseEntity.ok(alertRepository.save(alert));
    }
}