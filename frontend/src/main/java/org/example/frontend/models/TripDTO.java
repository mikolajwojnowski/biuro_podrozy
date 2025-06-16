package org.example.frontend.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TripDTO {
    private Long id;
    private String name;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private Integer capacity;
    private Integer availableSpots;

    public TripDTO() {
    }

    public TripDTO(Long id, String name, String destination, LocalDate startDate, LocalDate endDate, 
                   BigDecimal price, Integer capacity, Integer availableSpots) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.capacity = capacity;
        this.availableSpots = availableSpots;
    }

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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }
} 