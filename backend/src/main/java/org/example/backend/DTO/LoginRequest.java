package org.example.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Klasa reprezentująca żądanie logowania użytkownika.
 * Zawiera dane niezbędne do uwierzytelnienia użytkownika.
 */
public class LoginRequest {
    /**
     * Adres email użytkownika.
     * Pole wymagane, musi być poprawnym adresem email.
     */
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * Hasło użytkownika.
     * Pole wymagane, minimalna długość to 8 znaków.
     */
    @NotBlank(message = "Password is required")
    @Size(min=8)
    private String password;

    /**
     * Zwraca adres email użytkownika.
     * @return adres email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres email użytkownika.
     * @param email adres email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Zwraca hasło użytkownika.
     * @return hasło
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika.
     * @param password hasło
     */
    public void setPassword(String password) {
        this.password = password;
    }
} 