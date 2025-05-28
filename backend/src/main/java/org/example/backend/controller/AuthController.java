package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.ApiResponse;
import org.example.backend.DTO.ErrorResponse;
import org.example.backend.DTO.LoginRequest;
import org.example.backend.DTO.LoginResponse;
import org.example.backend.service.UserService;
import org.example.backend.models.User;
import org.example.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.isRole()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid email or password"));
        }
    }
} 