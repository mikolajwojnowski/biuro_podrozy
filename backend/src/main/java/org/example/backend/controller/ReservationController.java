package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.models.Reservation;
import org.example.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// RESERVATION CONTROLLER
/// /api/reservations

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    ///reservation service object
    private final ReservationService reservationService;


    ///constructor
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /// POST /api/reservations  - creates a new reservation for a trip (requires authentication - jwt token)
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.createReservation(reservation));
    }


    /// GET /api/reservations/trip/{tripId}  - list all reservations for a trip (requires authentication - jwt token)
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Reservation>> getReservationsForTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(reservationService.getReservationsForTrip(tripId));
    }

    /// GET /api/reservations/{id}  - gets a specific reservation (requires authentication - jwt token)
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    /// DELETE /api/reservations/{id}  - soft delete, cancels reservation by changing isActive value (requires authentication - jwt token)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }
} 