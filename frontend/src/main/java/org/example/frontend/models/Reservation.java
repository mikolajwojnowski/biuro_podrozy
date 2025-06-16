package org.example.frontend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private Long id;
    private String name;
    private String surname;
    private String email;
    
    @JsonProperty("phone_number")
    private String phoneNumber;
    
    private Trip trip;
    private boolean isActive = true;
    private List<ParticipantDTO> participants = new ArrayList<>();

    // Constructors
    public Reservation() {
    }

    public Reservation(String name, String surname, String email, String phoneNumber, Trip trip) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.trip = trip;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }

    public void addParticipant(ParticipantDTO participant) {
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        this.participants.add(participant);
    }
} 