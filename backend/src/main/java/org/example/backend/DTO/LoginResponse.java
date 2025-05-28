package org.example.backend.DTO;

public class LoginResponse implements ApiResponse {
    private String token;
    private String email;
    private boolean role;

    public LoginResponse(String token, String email, boolean role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }
} 