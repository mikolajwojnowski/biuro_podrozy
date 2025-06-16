package org.example.frontend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private boolean role;

    // Required no-arg constructor
    public User() {}

    // Full constructor
    public User(String email, String password, boolean role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isRole() { return role; }
    public void setRole(boolean role) { this.role = role; }
}