package org.example.backend.DTO;

import jakarta.validation.constraints.NotBlank;

/**
 * Klasa reprezentująca żądanie zmiany hasła użytkownika.
 * Zawiera stare i nowe hasło użytkownika.
 */
public class ChangePasswordRequest {
    /**
     * Stare hasło użytkownika.
     * Pole wymagane, nie może być puste.
     */
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    /**
     * Nowe hasło użytkownika.
     * Pole wymagane, nie może być puste.
     */
    @NotBlank(message = "New password is required")
    private String newPassword;

    /**
     * Zwraca stare hasło użytkownika.
     * @return stare hasło
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Ustawia stare hasło użytkownika.
     * @param oldPassword stare hasło
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Zwraca nowe hasło użytkownika.
     * @return nowe hasło
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Ustawia nowe hasło użytkownika.
     * @param newPassword nowe hasło
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
} 