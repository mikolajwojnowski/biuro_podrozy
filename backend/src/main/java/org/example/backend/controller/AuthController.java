package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.DTO.ApiResponse;
import org.example.backend.DTO.ErrorResponse;
import org.example.backend.DTO.LoginResponse;
import org.example.backend.DTO.ChangePasswordRequest;
import org.example.backend.service.UserService;
import org.example.backend.models.User;
import org.example.backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Kontroler obsługujący operacje związane z autoryzacją i uwierzytelnianiem użytkowników.
 * Zapewnia endpointy do logowania, rejestracji i odświeżania tokenów.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Konstruktor wstrzykujący zależności.
     * @param userService serwis użytkowników
     * @param jwtUtil narzędzie do obsługi tokenów JWT
     */
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Endpoint do rejestracji nowego użytkownika.
     * @param registrationData dane rejestracji (email, hasło i powtórzone hasło)
     * @return odpowiedź informująca o statusie rejestracji
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody Map<String, String> registrationData) {
        try {
            String email = registrationData.get("email");
            String password = registrationData.get("password");
            String confirmPassword = registrationData.get("confirmPassword");

            if (!password.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Passwords do not match"));
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(false); // Set as regular user by default

            User savedUser = userService.createUser(user);
            
            // Generate tokens for the newly registered user
            String accessToken = jwtUtil.generateAccessToken(savedUser);
            String refreshToken = jwtUtil.generateRefreshToken(savedUser);
            
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("email", savedUser.getEmail());
            tokens.put("role", savedUser.isRole() ? "ADMIN" : "USER");
            tokens.put("userId", savedUser.getId());
            
            return ResponseEntity.ok(new LoginResponse(tokens));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An error occurred during registration"));
        }
    }

    /**
     * Endpoint do logowania użytkownika.
     * @param credentials dane logowania (email i hasło)
     * @return odpowiedź zawierająca tokeny dostępu i odświeżania oraz dane użytkownika
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            User user = userService.findByEmail(email, password);
            
            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            System.out.println("Login - User role: " + (user.isRole() ? "ADMIN" : "USER"));
            
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("email", user.getEmail());
            tokens.put("role", user.isRole() ? "ADMIN" : "USER");
            tokens.put("userId", user.getId());
            
            return ResponseEntity.ok(new LoginResponse(tokens));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid email or password"));
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An error occurred during login"));
        }
    }

    /**
     * Endpoint do odświeżania tokenu dostępu.
     * @param refreshToken token odświeżania
     * @return odpowiedź zawierająca nowy token dostępu
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid token format"));
            }

            // Validate the refresh token
            String tokenType = jwtUtil.extractTokenType(refreshToken);
            if (!"REFRESH".equals(tokenType)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid token type"));
            }

            String userEmail = jwtUtil.extractEmail(refreshToken);
            User user = userService.findByEmail(userEmail);

            if (jwtUtil.validateToken(refreshToken, userEmail)) {
                String newAccessToken = jwtUtil.generateAccessToken(user);
                
                Map<String, String> response = new HashMap<>();
                response.put("accessToken", newAccessToken);
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid refresh token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error processing refresh token"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        try {
            userService.changePassword(authentication.getName(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 