package org.example.backend.DTO;

import java.math.BigDecimal;

public class ReservationResponseDTO {
    private Long id;
    private String userEmail;
    private Integer numberOfPeople;
    private BigDecimal totalPrice;
    private boolean active;

    public ReservationResponseDTO(Long id, String userEmail, Integer numberOfPeople, BigDecimal totalPrice, boolean active) {
        this.id = id;
        this.userEmail = userEmail;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public boolean isActive() {
        return active;
    }
} 