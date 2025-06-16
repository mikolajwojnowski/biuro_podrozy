package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.models.Reservation;
import org.example.backend.DTO.ReservationDTO;
import org.example.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler obsługujący operacje związane z rezerwacjami.
 * Zapewnia endpointy do tworzenia, pobierania, aktualizacji i usuwania rezerwacji.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    /**
     * Serwis obsługujący logikę biznesową rezerwacji.
     */
    private final ReservationService reservationService;

    /**
     * Konstruktor wstrzykujący zależności.
     * @param reservationService serwis rezerwacji
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Endpoint do tworzenia nowej rezerwacji.
     * @param reservationDTO dane rezerwacji
     * @return odpowiedź zawierająca utworzoną rezerwację
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        return ResponseEntity.ok(reservationService.createReservation(reservationDTO));
    }

    /**
     * Endpoint do pobierania wszystkich rezerwacji.
     * @return lista wszystkich rezerwacji
     */
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Reservation>> getReservationsForTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(reservationService.getReservationsForTrip(tripId));
    }

    /**
     * Endpoint do pobierania rezerwacji po identyfikatorze.
     * @param id identyfikator rezerwacji
     * @return rezerwacja o podanym identyfikatorze
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    /**
     * Endpoint do usuwania rezerwacji.
     * @param id identyfikator rezerwacji
     * @return odpowiedź informująca o statusie usunięcia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint do generowania PDF dla rezerwacji.
     * @param id identyfikator rezerwacji
     * @return PDF rezerwacji
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateReservationPdf(@PathVariable Long id) {
        byte[] pdfContent = reservationService.generateReservationPdf(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=reservation_" + id + ".pdf")
                .body(pdfContent);
    }
} 