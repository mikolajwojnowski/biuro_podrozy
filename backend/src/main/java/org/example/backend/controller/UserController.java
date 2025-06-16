package org.example.backend.controller;

import org.example.backend.DTO.UserResponseDTO;
import org.example.backend.service.UserService;
import org.example.backend.models.User;
import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/// USER CONTROLLER
/// /api/users

@RestController
@RequestMapping("/api/users")
public class UserController {

    ///success message
    private static final String USER_CREATION_SUCCESS = "User created successfully.";

    ///logger
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    ///user service instance
    private final UserService userService;

    ///constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /// POST /api/users  - creates a new user account (Public - no authentication needed)
    @PostMapping
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User user) {
        try {
            logger.info("Attempting to create user with email: {}", user.getEmail());
            User savedUser = userService.createUser(user);
            logger.info("Successfully created user with email: {}", savedUser.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("message", USER_CREATION_SUCCESS);
            response.put("user", savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create user: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            return handleException(e);
        }
    }

    /// GET api/users/email/{email}  - gets user details by email (requires authentication - jwt token)
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<?> handleException(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

