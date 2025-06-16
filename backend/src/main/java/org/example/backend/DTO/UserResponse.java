package org.example.backend.DTO;

/**
 * Klasa reprezentująca odpowiedź z danymi użytkownika.
 * Zawiera informacje o użytkowniku, takie jak identyfikator, email i rola.
 */
public class UserResponse {
    /**
     * Identyfikator użytkownika.
     */
    private Long id;

    /**
     * Adres email użytkownika.
     */
    private String email;

    /**
     * Rola użytkownika (true = admin, false = zwykły użytkownik).
     */
    private boolean role;

    /**
     * Domyślny konstruktor.
     */
    public UserResponse() {
    }

    /**
     * Konstruktor z parametrami.
     * @param id identyfikator użytkownika
     * @param email adres email użytkownika
     * @param role rola użytkownika
     */
    public UserResponse(Long id, String email, boolean role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    /**
     * Zwraca identyfikator użytkownika.
     * @return identyfikator użytkownika
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia identyfikator użytkownika.
     * @param id identyfikator użytkownika
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Zwraca adres email użytkownika.
     * @return adres email użytkownika
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres email użytkownika.
     * @param email adres email użytkownika
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Zwraca rolę użytkownika.
     * @return true jeśli admin, false jeśli zwykły użytkownik
     */
    public boolean isRole() {
        return role;
    }

    /**
     * Ustawia rolę użytkownika.
     * @param role true jeśli admin, false jeśli zwykły użytkownik
     */
    public void setRole(boolean role) {
        this.role = role;
    }
} 