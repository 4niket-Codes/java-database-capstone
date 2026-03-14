package com.project.back_end.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${jwt.secret:smartclinic-secret-key-2024-ibm-capstone-project-longkey}")
    private String secretKeyString;

    @Value("${jwt.expiration:86400000}")
    private long expirationTime;

    /**
     * Returns a SecretKey derived from the configured secret string.
     * Ensures the key is at least 512 bits (64 bytes) for HS512.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) {
            keyBytes = Arrays.copyOf(keyBytes, 64);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("username", username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(stripBearer(token));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean validateAdminToken(String token) {
        return validateTokenWithRole(token, "ADMIN");
    }

    public boolean validateDoctorToken(String token) {
        return validateTokenWithRole(token, "DOCTOR");
    }

    public boolean validatePatientToken(String token) {
        return validateTokenWithRole(token, "PATIENT");
    }

    private boolean validateTokenWithRole(String token, String requiredRole) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(stripBearer(token)).getBody();
            return requiredRole.equalsIgnoreCase(claims.get("role", String.class));
        } catch (Exception e) { return false; }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(stripBearer(token)).getBody().getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(stripBearer(token)).getBody().get("role", String.class);
    }

    private String stripBearer(String token) {
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : token;
    }
}
