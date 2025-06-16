package org.example.backend.DTO;

import java.util.Map;

/**
 * Klasa reprezentująca odpowiedź po pomyślnym zalogowaniu użytkownika.
 * Zawiera tokeny dostępu i odświeżania oraz dane użytkownika.
 * Implementuje interfejs ApiResponse.
 */
public class LoginResponse implements ApiResponse {
    /**
     * Token dostępu JWT.
     */
    private String accessToken;

    /**
     * Token odświeżania JWT.
     */
    private String refreshToken;

    /**
     * Adres email zalogowanego użytkownika.
     */
    private String email;

    /**
     * Rola zalogowanego użytkownika (ADMIN/USER).
     */
    private String role;

    /**
     * Identyfikator zalogowanego użytkownika.
     */
    private Long userId;

    /**
     * Tworzy nową odpowiedź logowania na podstawie mapy danych.
     * @param data mapa zawierająca dane odpowiedzi
     */
    public LoginResponse(Map<String, Object> data) {
        this.accessToken = (String) data.get("accessToken");
        this.refreshToken = (String) data.get("refreshToken");
        this.email = (String) data.get("email");
        this.role = (String) data.get("role");
        this.userId = (Long) data.get("userId");
    }

    /**
     * Zwraca token dostępu JWT.
     * @return token dostępu
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Zwraca token odświeżania JWT.
     * @return token odświeżania
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Zwraca adres email zalogowanego użytkownika.
     * @return adres email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Zwraca rolę zalogowanego użytkownika.
     * @return rola użytkownika (ADMIN/USER)
     */
    public String getRole() {
        return role;
    }

    /**
     * Zwraca identyfikator zalogowanego użytkownika.
     * @return identyfikator użytkownika
     */
    public Long getUserId() {
        return userId;
    }
} 