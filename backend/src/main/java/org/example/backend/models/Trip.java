package org.example.backend.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Klasa reprezentująca wycieczkę.
 * Zawiera informacje o wycieczce, takie jak tytuł, opis, data, pojemność i cena.
 */
@Entity
@Table(name = "trips")
public class Trip {
    /**
     * Identyfikator wycieczki.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tytuł wycieczki.
     * Maksymalna długość: 100 znaków.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String title;

    /**
     * Opis wycieczki.
     * Maksymalna długość: 500 znaków.
     */
    @Column(nullable = true, length = 500)
    private String description;

    /**
     * Data wycieczki.
     * Format: yyyy-MM-dd
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "trip_date", nullable = false)
    private LocalDate tripDate;

    /**
     * Maksymalna liczba uczestników.
     * Wartość minimalna: 1
     */
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private int capacity;

    /**
     * Liczba dostępnych miejsc.
     */
    @Column(name = "available_spots", nullable = false)
    private int availableSpots;

    /**
     * Status aktywności wycieczki.
     */
    @Basic
    @Column(name = "isActive", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isActive = true;

    /**
     * Liczba dni trwania wycieczki.
     */
    @Column(nullable = true)
    private int days;

    /**
     * Cena wycieczki.
     */
    @Column(nullable = true)
    private double price;

    /**
     * Domyślny konstruktor.
     */
    public Trip() {
    }

    /**
     * Tworzy nową wycieczkę z podanymi danymi.
     * @param title tytuł wycieczki
     * @param description opis wycieczki
     * @param tripDate data wycieczki
     * @param capacity maksymalna liczba uczestników
     * @param isActive status aktywności
     * @param days liczba dni trwania
     * @param price cena wycieczki
     */
    public Trip(String title, String description, LocalDate tripDate, int capacity, boolean isActive, int days, double price) {
        this.title = title;
        this.description = description;
        this.tripDate = tripDate;
        this.capacity = capacity;
        this.isActive = isActive;
        this.days = days;
        this.price = price;
    }

    /**
     * Zwraca identyfikator wycieczki.
     * @return identyfikator wycieczki
     */
    public Long getId() {
        return id;
    }

    /**
     * Zwraca tytuł wycieczki.
     * @return tytuł wycieczki
     */
    public String getTitle() {
        return title;
    }

    /**
     * Zwraca opis wycieczki.
     * @return opis wycieczki
     */
    public String getDescription() {
        return description;
    }

    /**
     * Zwraca datę wycieczki.
     * @return data wycieczki
     */
    public LocalDate getTripDate() {
        return tripDate;
    }

    /**
     * Sprawdza czy wycieczka jest aktywna.
     * @return true jeśli wycieczka jest aktywna, false w przeciwnym razie
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Zwraca maksymalną liczbę uczestników.
     * @return maksymalna liczba uczestników
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Zwraca liczbę dostępnych miejsc.
     * @return liczba dostępnych miejsc
     */
    public int getAvailableSpots() {
        return availableSpots;
    }

    /**
     * Zwraca liczbę dni trwania wycieczki.
     * @return liczba dni
     */
    public int getDays() {
        return days;
    }

    /**
     * Zwraca cenę wycieczki.
     * @return cena wycieczki
     */
    public double getPrice() {
        return price;
    }

    /**
     * Ustawia tytuł wycieczki.
     * @param title tytuł wycieczki
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Ustawia opis wycieczki.
     * @param description opis wycieczki
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Ustawia datę wycieczki.
     * @param tripDate data wycieczki
     */
    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    /**
     * Ustawia maksymalną liczbę uczestników.
     * Jeśli nowa pojemność jest mniejsza niż liczba dostępnych miejsc,
     * liczba dostępnych miejsc zostanie automatycznie zaktualizowana.
     * @param capacity maksymalna liczba uczestników
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
        if (this.availableSpots > capacity) {
            this.availableSpots = capacity;
        }
    }

    /**
     * Ustawia liczbę dostępnych miejsc.
     * Liczba dostępnych miejsc nie może przekraczać maksymalnej pojemności.
     * @param availableSpots liczba dostępnych miejsc
     */
    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = Math.min(availableSpots, this.capacity);
    }

    /**
     * Ustawia status aktywności wycieczki.
     * @param active status aktywności
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Ustawia liczbę dni trwania wycieczki.
     * @param days liczba dni
     */
    public void setDays(int days) {
        this.days = days;
    }

    /**
     * Ustawia cenę wycieczki.
     * @param price cena wycieczki
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
