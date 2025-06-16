package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca użytkownika systemu.
 * Zawiera informacje o użytkowniku, takie jak email, hasło, rola i status aktywności.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identyfikator użytkownika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Adres email użytkownika.
     */
    @JsonProperty("email") // Wymusza odpowiednią nazwę w JSON
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true, length = 60)
    private String email;

    /**
     * Hasło użytkownika.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * Rola użytkownika (true = admin, false = zwykły użytkownik).
     */
    @Column(nullable = false)
    private boolean role = false; // false = USER, true = ADMIN

    /**
     * Status aktywności użytkownika.
     */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    /**
     * Lista rezerwacji powiązanych z użytkownikiem.
     */
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Reservation> reservations = new ArrayList<>();

    /**
     * Domyślny konstruktor.
     */
    public User() {
    }

    /**
     * Konstruktor z parametrami.
     * @param password hasło użytkownika
     * @param email adres email użytkownika
     * @param role rola użytkownika
     */
    public User(String password, String email, boolean role) {
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Zwraca uprawnienia użytkownika na podstawie roli.
     * @return kolekcja uprawnień użytkownika
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role) {
            return List.of(
                new SimpleGrantedAuthority("ADMIN"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
            );
        } else {
            return List.of(
                new SimpleGrantedAuthority("USER"),
                new SimpleGrantedAuthority("ROLE_USER")
            );
        }
    }

    // Custom getter for role to make it more readable
    // ----- Getters and Setters -----

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
     * Zwraca hasło użytkownika.
     * @return hasło użytkownika
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika.
     * @param password hasło użytkownika
     */
    public void setPassword(String password) {
        this.password = password;
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

    /**
     * Zwraca status aktywności użytkownika.
     * @return true jeśli aktywny, false jeśli nieaktywny
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Ustawia status aktywności użytkownika.
     * @param active true jeśli aktywny, false jeśli nieaktywny
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
