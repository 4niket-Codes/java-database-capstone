package com.project.back_end.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${jwt.secret:smartclinic-secret-key-2024-ibm-capstone}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private long expirationTime;

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            token = stripBearer(token);
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
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
            token = stripBearer(token);
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            String role = claims.get("role", String.class);
            return requiredRole.equalsIgnoreCase(role);
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        token = stripBearer(token);
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String extractRole(String token) {
        token = stripBearer(token);
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    private String stripBearer(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
