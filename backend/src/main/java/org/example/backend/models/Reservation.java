package org.example.backend.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca rezerwację wycieczki.
 * Zawiera informacje o rezerwacji, takie jak identyfikator, użytkownik, wycieczka, liczba osób, cena i status aktywności.
 */
@Entity
@Table(name = "reservations")
public class Reservation {
    /**
     * Identyfikator rezerwacji.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Użytkownik, który dokonał rezerwacji.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    /**
     * Wycieczka, na którą dokonano rezerwacji.
     */
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false, length = 50)
    private String name; // Main contact person's name

    @Column(length = 50)
    private String surname; // Main contact person's surname

    @Column(length = 60)
    private String email; // Main contact person's email

    @Column(name = "phone_number", length = 15)
    private String phoneNumber; // Main contact person's phone

    /**
     * Liczba osób w rezerwacji.
     */
    @Column(nullable = false)
    private Integer numberOfPeople;

    /**
     * Całkowita cena rezerwacji.
     */
    @Column(nullable = false)
    private BigDecimal totalPrice;

    /**
     * Status aktywności rezerwacji.
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * Lista uczestników rezerwacji.
     */
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReservationParticipant> participants = new ArrayList<>();

    /**
     * Domyślny konstruktor.
     */
    public Reservation() {
    }

    /**
     * Konstruktor z parametrami.
     * @param user użytkownik
     * @param trip wycieczka
     * @param numberOfPeople liczba osób
     * @param totalPrice całkowita cena
     */
    public Reservation(User user, Trip trip, Integer numberOfPeople, BigDecimal totalPrice) {
        this.user = user;
        this.trip = trip;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
    }

    // Helper method to add a participant
    public void addParticipant(ReservationParticipant participant) {
        participants.add(participant);
        participant.setReservation(this);
    }

    // Helper method to remove a participant
    public void removeParticipant(ReservationParticipant participant) {
        participants.remove(participant);
        participant.setReservation(null);
    }

    /**
     * Zwraca identyfikator rezerwacji.
     * @return identyfikator rezerwacji
     */
    public Long getId() {
        return id;
    }

    /**
     * Ustawia identyfikator rezerwacji.
     * @param id identyfikator rezerwacji
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Zwraca użytkownika, który dokonał rezerwacji.
     * @return użytkownik
     */
    public User getUser() {
        return user;
    }

    /**
     * Ustawia użytkownika, który dokonał rezerwacji.
     * @param user użytkownik
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Zwraca wycieczkę, na którą dokonano rezerwacji.
     * @return wycieczka
     */
    public Trip getTrip() {
        return trip;
    }

    /**
     * Ustawia wycieczkę, na którą dokonano rezerwacji.
     * @param trip wycieczka
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Zwraca liczbę osób w rezerwacji.
     * @return liczba osób
     */
    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    /**
     * Ustawia liczbę osób w rezerwacji.
     * @param numberOfPeople liczba osób
     */
    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    /**
     * Zwraca całkowitą cenę rezerwacji.
     * @return całkowita cena
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * Ustawia całkowitą cenę rezerwacji.
     * @param totalPrice całkowita cena
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Zwraca status aktywności rezerwacji.
     * @return true jeśli aktywna, false jeśli nieaktywna
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Ustawia status aktywności rezerwacji.
     * @param active true jeśli aktywna, false jeśli nieaktywna
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Zwraca listę uczestników rezerwacji.
     * @return lista uczestników
     */
    public List<ReservationParticipant> getParticipants() {
        return participants;
    }

    /**
     * Ustawia listę uczestników rezerwacji.
     * @param participants lista uczestników
     */
    public void setParticipants(List<ReservationParticipant> participants) {
        this.participants = participants;
    }
} 