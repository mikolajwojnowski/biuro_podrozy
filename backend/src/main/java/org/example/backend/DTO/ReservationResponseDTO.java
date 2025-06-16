package org.example.backend.DTO;

import java.math.BigDecimal;

/**
 * Klasa reprezentująca odpowiedź z informacjami o rezerwacji.
 * Zawiera podstawowe dane rezerwacji do wyświetlenia.
 */
public class ReservationResponseDTO {
    /**
     * Identyfikator rezerwacji.
     */
    private Long id;

    /**
     * Adres email użytkownika, który dokonał rezerwacji.
     */
    private String userEmail;

    /**
     * Liczba osób w rezerwacji.
     */
    private Integer numberOfPeople;

    /**
     * Całkowita cena rezerwacji.
     */
    private BigDecimal totalPrice;

    /**
     * Status aktywności rezerwacji.
     */
    private boolean active;

    /**
     * Tworzy nową odpowiedź z informacjami o rezerwacji.
     * @param id identyfikator rezerwacji
     * @param userEmail adres email użytkownika
     * @param numberOfPeople liczba osób
     * @param totalPrice całkowita cena
     * @param active status aktywności
     */
    public ReservationResponseDTO(Long id, String userEmail, Integer numberOfPeople, BigDecimal totalPrice, boolean active) {
        this.id = id;
        this.userEmail = userEmail;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
        this.active = active;
    }

    /**
     * Zwraca identyfikator rezerwacji.
     * @return identyfikator rezerwacji
     */
    public Long getId() {
        return id;
    }

    /**
     * Zwraca adres email użytkownika.
     * @return adres email użytkownika
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Zwraca liczbę osób w rezerwacji.
     * @return liczba osób
     */
    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    /**
     * Zwraca całkowitą cenę rezerwacji.
     * @return całkowita cena
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sprawdza czy rezerwacja jest aktywna.
     * @return true jeśli rezerwacja jest aktywna, false w przeciwnym razie
     */
    public boolean isActive() {
        return active;
    }
} 