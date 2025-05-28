package org.example.backend.controller;

import org.example.backend.DTO.UserResponseDTO;
import org.example.backend.models.User;
import org.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin") // Base path for all admin endpoints
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        // Convert List<User> to List<UserResponseDTO>
        List<UserResponseDTO> response = users.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.isRole() ? "ADMIN" : "USER" // Assuming isRole() returns boolean
                ))
                .toList();

        return ResponseEntity.ok(response);
    }
}