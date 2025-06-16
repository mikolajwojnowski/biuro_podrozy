package org.example.backend.DTO;

/**
 * Klasa reprezentująca odpowiedź zawierającą informację o błędzie.
 * Implementuje interfejs ApiResponse.
 */
public class ErrorResponse implements ApiResponse {
    /**
     * Wiadomość opisująca błąd.
     */
    private String message;

    /**
     * Tworzy nową odpowiedź z informacją o błędzie.
     * @param message wiadomość opisująca błąd
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * Zwraca wiadomość opisującą błąd.
     * @return wiadomość o błędzie
     */
    public String getMessage() {
        return message;
    }

    /**
     * Ustawia wiadomość opisującą błąd.
     * @param message wiadomość o błędzie
     */
    public void setMessage(String message) {
        this.message = message;
    }
} 