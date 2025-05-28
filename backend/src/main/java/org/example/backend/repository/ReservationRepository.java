package org.example.backend.repository;

import org.example.backend.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTripId(Long tripId);
    List<Reservation> findByTripIdAndActive(Long tripId, boolean active);
} 