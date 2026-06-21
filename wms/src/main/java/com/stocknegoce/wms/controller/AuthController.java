package com.stocknegoce.wms.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocknegoce.wms.model.User;
import com.stocknegoce.wms.model.DTO.LoginRequest;
import com.stocknegoce.wms.repository.global.UserRepository;
import com.stocknegoce.wms.service.JwtService;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtService jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // ── Étape 1 : Chercher l'utilisateur par login ────────────────────────
        Optional<User> userOptional = userRepository.findByLogin(request.getLogin());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login introuvable");
        }

        User user = userOptional.get();

        // ── Étape 2 : Vérifier le mot de passe avec BCrypt ───────────────────
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect");
        }

        // ── Étape 3 : Générer le token JWT ───────────────────────────────────
        String token = jwtService.generateToken(user.getLogin(), user.getId_user());

        return ResponseEntity.ok(new AuthResponse(token, user.getId_user(), user.getFirstName(), user.getRole()));
    }

    @Getter
    @Setter
    public static class AuthResponse {
        private String token;
        private Integer userId;
        private String firstName;
        private String role;

        public AuthResponse(String token, Integer userId, String firstName, String role) {
            this.token = token;
            this.userId = userId;
            this.firstName = firstName;
            this.role = role;
        }
    }
}