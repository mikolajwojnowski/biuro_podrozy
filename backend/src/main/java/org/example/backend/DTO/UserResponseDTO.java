package org.example.backend.DTO;

public class UserResponseDTO {
    private Long id;
    private String email;
    private String roleName;

    public UserResponseDTO(Long id, String email, String roleName) {
        this.id = id;
        this.email = email;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleName() {
        return roleName;
    }
}