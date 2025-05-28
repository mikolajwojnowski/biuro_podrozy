package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.models.Trip;
import org.example.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// TRIP CONTROLLER

/// /api/trips

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    /// constructor
    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /// GET /api/trips  - lists all available trips (requires authentication - jwt token)
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    /// GET /api/trips/{id}  - gets details of a specific trip (requires authentication - jwt token)
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTrip(id));
    }

    /// POST /api/trips  - creates new trip (Admin) (requires authentication - jwt token)
    @PostMapping
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip) {
        return ResponseEntity.ok(tripService.createTrip(trip));
    }

    /// PUT /api/trips/{id}   - updates a trip (Admin) (requires authentication - jwt token)
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @Valid @RequestBody Trip trip) {
        return ResponseEntity.ok(tripService.updateTrip(id, trip));
    }

    /// DELETE /api/trips/{id}  - soft delete a trip (Admin) (requires authentication - jwt token)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok().build();
    }
}
