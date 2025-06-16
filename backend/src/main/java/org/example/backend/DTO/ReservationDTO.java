package org.example.backend.DTO;

import java.util.List;

/**
 * Klasa reprezentująca dane transferowe dla rezerwacji.
 * Zawiera informacje potrzebne do utworzenia nowej rezerwacji.
 */
public class ReservationDTO {
    /**
     * Identyfikator wycieczki.
     */
    private Long tripId;

    /**
     * Identyfikator użytkownika.
     */
    private Long userId;

    /**
     * Imię osoby kontaktowej.
     */
    private String name;            // Main contact person

    /**
     * Nazwisko osoby kontaktowej.
     */
    private String surname;         // Main contact person

    /**
     * Adres email osoby kontaktowej.
     */
    private String email;           // Main contact person

    /**
     * Numer telefonu osoby kontaktowej.
     */
    private String phoneNumber;     // Main contact person

    /**
     * Lista uczestników rezerwacji.
     */
    private List<ParticipantDTO> participants;

    /**
     * Domyślny konstruktor.
     */
    public ReservationDTO() {
    }

    /**
     * Zwraca identyfikator wycieczki.
     * @return identyfikator wycieczki
     */
    public Long getTripId() {
        return tripId;
    }

    /**
     * Ustawia identyfikator wycieczki.
     * @param tripId identyfikator wycieczki
     */
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    /**
     * Zwraca identyfikator użytkownika.
     * @return identyfikator użytkownika
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Ustawia identyfikator użytkownika.
     * @param userId identyfikator użytkownika
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Zwraca imię osoby kontaktowej.
     * @return imię osoby kontaktowej
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię osoby kontaktowej.
     * @param name imię osoby kontaktowej
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca nazwisko osoby kontaktowej.
     * @return nazwisko osoby kontaktowej
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Ustawia nazwisko osoby kontaktowej.
     * @param surname nazwisko osoby kontaktowej
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Zwraca adres email osoby kontaktowej.
     * @return adres email osoby kontaktowej
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres email osoby kontaktowej.
     * @param email adres email osoby kontaktowej
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Zwraca numer telefonu osoby kontaktowej.
     * @return numer telefonu osoby kontaktowej
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Ustawia numer telefonu osoby kontaktowej.
     * @param phoneNumber numer telefonu osoby kontaktowej
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Zwraca listę uczestników rezerwacji.
     * @return lista uczestników
     */
    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    /**
     * Ustawia listę uczestników rezerwacji.
     * @param participants lista uczestników
     */
    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }
} 