package com.example.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;
import com.example.backend.entity.Role;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "DocumentManagementSystemSecretKey2026";

    // Generate JWT Token
   // Generate JWT Token with Email + Role
public String generateToken(String email, Role role) {

    return Jwts.builder()
            .subject(email)
            .claim("role", role.name())
            .issuedAt(new Date())
            .expiration(
                    new Date(System.currentTimeMillis() + 86400000)
            )
            .signWith(
                    io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                            SECRET_KEY.getBytes()
                    ),
                    SignatureAlgorithm.HS256
            )
            .compact();
}

    // Extract Email
    public String extractEmail(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(
                        io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Validate Token
    public boolean validateToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith(
                            io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                    SECRET_KEY.getBytes()
                            )
                    )
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

}