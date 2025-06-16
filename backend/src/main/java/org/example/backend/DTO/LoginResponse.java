package org.example.backend.DTO;

import java.util.Map;

public class LoginResponse implements ApiResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String role;
    private Long userId;

    public LoginResponse(Map<String, Object> data) {
        this.accessToken = (String) data.get("accessToken");
        this.refreshToken = (String) data.get("refreshToken");
        this.email = (String) data.get("email");
        this.role = (String) data.get("role");
        this.userId = (Long) data.get("userId");
    }

    // Getters
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }
} 