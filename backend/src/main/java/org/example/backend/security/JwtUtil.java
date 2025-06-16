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

/**
 * Klasa narzędziowa do obsługi tokenów JWT.
 * Zapewnia metody do generowania, weryfikacji i odczytywania tokenów JWT.
 */
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

    /**
     * Generuje token dostępu dla użytkownika.
     * @param user dane użytkownika
     * @return wygenerowany token dostępu
     */
    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration);
    }

    /**
     * Generuje token odświeżania dla użytkownika.
     * @param user dane użytkownika
     * @return wygenerowany token odświeżania
     */
    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration);
    }

    /**
     * Tworzy token JWT z podanymi danymi.
     * @param user dane użytkownika
     * @param expiration czas ważności tokenu w milisekundach
     * @return wygenerowany token JWT
     */
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

    /**
     * Wyodrębnia rolę użytkownika z tokenu JWT.
     * @param token token JWT
     * @return wyodrębniona rola użytkownika
     */
    public String extractRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        System.out.println("JWT Util - Extracted role from token: " + role);
        return role;
    }

    /**
     * Wyodrębnia typ tokenu z tokenu JWT.
     * @param token token JWT
     * @return wyodrębniony typ tokenu
     */
    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    /**
     * Sprawdza czy token JWT jest ważny dla danego użytkownika.
     * @param token token JWT do weryfikacji
     * @param userEmail email użytkownika
     * @return true jeśli token jest ważny, false w przeciwnym razie
     */
    public Boolean validateToken(String token, String userEmail) {
        try {
            final String email = extractEmail(token);
            return (email.equals(userEmail) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wyodrębnia email użytkownika z tokenu JWT.
     * @param token token JWT
     * @return wyodrębniony email użytkownika
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Wyodrębnia datę wygaśnięcia z tokenu JWT.
     * @param token token JWT
     * @return data wygaśnięcia tokenu
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Wyodrębnia dane z tokenu JWT.
     * @param token token JWT
     * @param claimsResolver funkcja do wyodrębnienia danych
     * @return wyodrębnione dane
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Wyodrębnia wszystkie dane z tokenu JWT.
     * @param token token JWT
     * @return wszystkie dane z tokenu
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Sprawdza czy token JWT wygasł.
     * @param token token JWT
     * @return true jeśli token wygasł, false w przeciwnym razie
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
} 