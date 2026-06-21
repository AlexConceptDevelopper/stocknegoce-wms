package com.stocknegoce.wms.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Service gérant la génération et la validation des tokens JWT.
 * Utilisé pour l'authentification des utilisateurs de l'appli mobile.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Génère la clé secrète à partir de la valeur dans application.properties
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Génère un token JWT pour un utilisateur connecté.
     * Le token contient le login et l'id de l'utilisateur.
     * Il expire après 48h.
     *
     * @param login  login de l'utilisateur
     * @param userId id de l'utilisateur
     * @return token JWT signé
     */
    public String generateToken(String login, Integer userId) {
        return Jwts.builder()
                .subject(login)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait les claims (données) d'un token JWT.
     * Lance une exception si le token est invalide ou expiré.
     *
     * @param token token JWT
     * @return claims du token
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extrait le login de l'utilisateur depuis le token.
     *
     * @param token token JWT
     * @return login de l'utilisateur
     */
    public String extractLogin(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Vérifie si un token JWT est valide (signature correcte et non expiré).
     *
     * @param token token JWT
     * @return true si valide, false sinon
     */
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
