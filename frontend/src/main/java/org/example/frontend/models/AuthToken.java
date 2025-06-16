package org.example.frontend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an authentication token received from the backend
 */

public class AuthToken {
    @JsonProperty("accessToken")
    private String accessToken;
    
    @JsonProperty("refreshToken")
    private String refreshToken;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("role")
    private String role;

    @JsonProperty("userId")
    private Long userId;

    @JsonCreator
    public AuthToken(
            @JsonProperty("accessToken") String accessToken,
            @JsonProperty("refreshToken") String refreshToken,
            @JsonProperty("email") String email,
            @JsonProperty("role") String role,
            @JsonProperty("userId") Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

    // Getters and setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}