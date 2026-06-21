package com.stocknegoce.wms.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stocknegoce.wms.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre JWT — intercepte chaque requête HTTP et vérifie le token.
 * S'exécute une seule fois par requête (OncePerRequestFilter).
 * Si le token est valide, l'utilisateur est authentifié dans Spring Security.
 * Si le token est absent ou invalide, la requête continue sans authentification
 * et Spring Security bloquera l'accès aux routes protégées.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // ── Étape 1 : Récupérer le header Authorization ───────────────────────
        // Le token arrive sous la forme : "Bearer eyJhbGci..."
        String authHeader = request.getHeader("Authorization");

        // ── Étape 2 : Vérifier que le header est présent et commence par Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Pas de token — on laisse passer, Spring Security gérera l'accès
            filterChain.doFilter(request, response);
            return;
        }

        // ── Étape 3 : Extraire le token (enlever "Bearer ")
        String token = authHeader.substring(7);

        // ── Étape 4 : Valider le token
        if (!jwtService.isTokenValid(token)) {
            // Token invalide ou expiré — on laisse passer sans authentifier
            filterChain.doFilter(request, response);
            return;
        }

        // ── Étape 5 : Extraire le login et authentifier l'utilisateur
        String login = jwtService.extractLogin(token);

        // ── Étape 6 : Enregistrer l'authentification dans Spring Security
        // null, null = pas de credentials, pas de roles pour l'instant
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(login, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ── Étape 7 : Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}