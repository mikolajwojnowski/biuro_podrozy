package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.models.Trip;
import org.example.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler obsługujący operacje związane z wycieczkami.
 * Zapewnia endpointy do tworzenia, pobierania, aktualizacji i usuwania wycieczek.
 */
@RestController
@RequestMapping("/api/trips")
public class TripController {

    /**
     * Serwis obsługujący logikę biznesową wycieczek.
     */
    private final TripService tripService;

    /**
     * Konstruktor wstrzykujący zależności.
     * @param tripService serwis wycieczek
     */
    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Endpoint do pobierania wszystkich wycieczek.
     * @return lista wszystkich wycieczek
     */
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    /**
     * Endpoint do pobierania wycieczki po identyfikatorze.
     * @param id identyfikator wycieczki
     * @return wycieczka o podanym identyfikatorze
     */
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTrip(id));
    }

    /**
     * Endpoint do tworzenia nowej wycieczki.
     * @param trip dane wycieczki
     * @return utworzona wycieczka
     */
    @PostMapping
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip) {
        return ResponseEntity.ok(tripService.createTrip(trip));
    }

    /**
     * Endpoint do aktualizacji wycieczki.
     * @param id identyfikator wycieczki
     * @param trip nowe dane wycieczki
     * @return zaktualizowana wycieczka
     */
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @Valid @RequestBody Trip trip) {
        return ResponseEntity.ok(tripService.updateTrip(id, trip));
    }

    /**
     * Endpoint do usuwania wycieczki.
     * @param id identyfikator wycieczki
     * @return odpowiedź informująca o statusie usunięcia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok().build();
    }
}
