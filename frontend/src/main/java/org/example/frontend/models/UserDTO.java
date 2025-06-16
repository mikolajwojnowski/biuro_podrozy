package org.example.frontend.models;

public class UserDTO {
    private Long id;
    private String email;
    private String roleName;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String roleName) {
        this.id = id;
        this.email = email;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return roleName;
    }

    public void setRole(String role) {
        this.roleName = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
} 