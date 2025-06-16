package org.example.backend.repository;

import org.example.backend.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfejs repozytorium dla encji Reservation.
 * Zapewnia metody do zarządzania rezerwacjami w bazie danych.
 * Spring Boot automatycznie dostarcza implementację repozytorium, dając dostęp do typowych metod
 * jak save(), findById(), findAll() i deleteById().
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /**
     * Znajduje wszystkie rezerwacje dla danej wycieczki.
     * @param tripId identyfikator wycieczki
     * @return lista rezerwacji dla danej wycieczki
     */
    List<Reservation> findByTripId(Long tripId);

    /**
     * Znajduje wszystkie aktywne rezerwacje dla danej wycieczki.
     * @param tripId identyfikator wycieczki
     * @return lista aktywnych rezerwacji dla danej wycieczki
     */
    List<Reservation> findByTripIdAndActiveTrue(Long tripId);

    /**
     * Znajduje wszystkie rezerwacje dla danej wycieczki o określonym statusie aktywności.
     * @param tripId identyfikator wycieczki
     * @param active status aktywności rezerwacji
     * @return lista rezerwacji dla danej wycieczki o określonym statusie
     */
    List<Reservation> findByTripIdAndActive(Long tripId, boolean active);

    /**
     * Znajduje wszystkie rezerwacje dla danego użytkownika.
     * @param userId identyfikator użytkownika
     * @return lista rezerwacji danego użytkownika
     */
    List<Reservation> findByUserId(Long userId);
} 