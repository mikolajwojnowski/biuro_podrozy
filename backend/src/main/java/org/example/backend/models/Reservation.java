package org.example.backend.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

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

    @Column(nullable = false)
    private Integer numberOfPeople;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReservationParticipant> participants = new ArrayList<>();

    public Reservation() {
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

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

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<ReservationParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ReservationParticipant> participants) {
        this.participants = participants;
    }
} 