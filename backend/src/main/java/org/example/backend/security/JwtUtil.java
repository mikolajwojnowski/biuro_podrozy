package org.example.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.backend.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret:your-default-secret-key-should-be-very-long-and-secure}")
    private String secret;

    @Value("${jwt.access-token.expiration:86400000}") // 24 hours in milliseconds
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}") // 7 days in milliseconds
    private long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration);
    }

    private String generateToken(User user, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        String role = user.isRole() ? "ADMIN" : "USER";
        role = role.toUpperCase();
        claims.put("role", role);
        claims.put("email", user.getEmail());
        claims.put("userId", user.getId());
        claims.put("type", expiration == accessTokenExpiration ? "ACCESS" : "REFRESH");
        
        System.out.println("JWT Util - Generating token with role: " + role);
        System.out.println("JWT Util - User role boolean: " + user.isRole());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        System.out.println("JWT Util - Extracted role from token: " + role);
        return role;
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public Boolean validateToken(String token, String userEmail) {
        try {
            final String email = extractEmail(token);
            return (email.equals(userEmail) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
} 