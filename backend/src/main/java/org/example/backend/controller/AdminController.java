package org.example.backend.controller;

import org.example.backend.DTO.UserResponseDTO;
import org.example.backend.DTO.ReservationResponseDTO;
import org.example.backend.models.User;
import org.example.backend.models.Reservation;
import org.example.backend.service.UserService;
import org.example.backend.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin") // Base path for all admin endpoints
public class AdminController {

    private final UserService userService;
    private final ReservationService reservationService;

    public AdminController(UserService userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        // Convert List<User> to List<UserResponseDTO>
        List<UserResponseDTO> response = users.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.isRole() ? "ADMIN" : "USER" // Assuming isRole() returns boolean
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/deactivate/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations/trip/{tripId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsForTrip(@PathVariable Long tripId) {
        List<Reservation> reservations = reservationService.getReservationsForTrip(tripId);
        List<ReservationResponseDTO> response = reservations.stream()
                .map(reservation -> new ReservationResponseDTO(
                        reservation.getId(),
                        reservation.getUser().getEmail(),
                        reservation.getNumberOfPeople(),
                        reservation.getTotalPrice(),
                        reservation.isActive()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservations/deactivate/{id}")
    public ResponseEntity<Void> deactivateReservation(@PathVariable Long id) {
        reservationService.deactivateReservation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations/pdf/{id}")
    public ResponseEntity<byte[]> generateReservationPdf(@PathVariable Long id) {
        byte[] pdfContent = reservationService.generateReservationPdf(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=reservation_" + id + ".pdf")
                .body(pdfContent);
    }
}