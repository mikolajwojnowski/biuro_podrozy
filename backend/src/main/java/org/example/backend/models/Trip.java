package org.example.backend.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(nullable = true, length = 500)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "trip_date", nullable = false)
    private LocalDate tripDate;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private int capacity;

    @Column(name = "available_spots", nullable = false)
    private int availableSpots;

    @Basic
    @Column(name = "isActive", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isActive = true;

    @Column(nullable = true)
    private int days;

    @Column(nullable = true)
    private double price;

    public Trip() {
    }

    public Trip(String title, String description, LocalDate tripDate, int capacity, boolean isActive, int days, double price) {
        this.title = title;
        this.description = description;
        this.tripDate = tripDate;
        this.capacity = capacity;
        this.isActive = isActive;
        this.days = days;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public int getDays() {
        return days;
    }

    public double getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        if (this.availableSpots > capacity) {
            this.availableSpots = capacity;
        }
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = Math.min(availableSpots, this.capacity);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
