package org.example.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Filtr do obsługi autentykacji JWT.
 * Sprawdza token JWT w nagłówku żądania i ustawia kontekst bezpieczeństwa.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Narzędzie do obsługi tokenów JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Konstruktor wstrzykujący zależności.
     * @param jwtUtil narzędzie do obsługi tokenów JWT
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Metoda filtrująca żądania HTTP.
     * Sprawdza token JWT w nagłówku i ustawia kontekst bezpieczeństwa.
     * @param request żądanie HTTP
     * @param response odpowiedź HTTP
     * @param filterChain łańcuch filtrów
     * @throws ServletException w przypadku błędu serwletu
     * @throws IOException w przypadku błędu wejścia/wyjścia
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                String userEmail = jwtUtil.extractEmail(jwt);
                String role = jwtUtil.extractRole(jwt);

                System.out.println("JWT Filter - Extracted role: " + role);

                if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(jwt, userEmail)) {
                        // Ensure role is uppercase
                        String upperRole = role.toUpperCase();
                        // Create authorities list with both ROLE_ prefix and without for compatibility
                        List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority(upperRole),
                            new SimpleGrantedAuthority("ROLE_" + upperRole)
                        );
                        
                        System.out.println("JWT Filter - Created authorities: " + authorities);
                        
                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Wyodrębnia token JWT z nagłówka żądania.
     * @param request żądanie HTTP
     * @return token JWT lub null, jeśli nie znaleziono
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 