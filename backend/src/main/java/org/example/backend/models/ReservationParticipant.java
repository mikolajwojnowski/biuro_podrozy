package org.example.backend.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Klasa reprezentująca uczestnika rezerwacji.
 * Zawiera informacje o osobie uczestniczącej w wycieczce.
 */
@Entity
@Table(name = "reservation_participants")
public class ReservationParticipant {
    /**
     * Identyfikator uczestnika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Rezerwacja, do której należy uczestnik.
     */
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    @JsonBackReference
    private Reservation reservation;

    /**
     * Imię uczestnika.
     * Maksymalna długość: 50 znaków.
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * Nazwisko uczestnika.
     * Maksymalna długość: 50 znaków.
     */
    @Column(length = 50)
    private String surname;

    /**
     * Domyślny konstruktor.
     */
    public ReservationParticipant() {
    }

    /**
     * Tworzy nowego uczestnika z podanymi danymi.
     * @param name imię uczestnika
     * @param surname nazwisko uczestnika
     */
    public ReservationParticipant(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    /**
     * Zwraca identyfikator uczestnika.
     * @return identyfikator uczestnika
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia identyfikator uczestnika.
     * @param id identyfikator uczestnika
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Zwraca rezerwację, do której należy uczestnik.
     * @return rezerwacja
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Ustawia rezerwację dla uczestnika.
     * @param reservation rezerwacja
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * Zwraca imię uczestnika.
     * @return imię uczestnika
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię uczestnika.
     * @param name imię uczestnika
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca nazwisko uczestnika.
     * @return nazwisko uczestnika
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Ustawia nazwisko uczestnika.
     * @param surname nazwisko uczestnika
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
} 