package org.example.backend.repository;

import org.example.backend.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfejs repozytorium dla encji Trip.
 * Zapewnia metody do zarządzania wycieczkami w bazie danych.
 * Spring Boot automatycznie dostarcza implementację repozytorium, dając dostęp do typowych metod
 * jak save(), findById(), findAll() i deleteById().
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    /**
     * Znajduje wszystkie aktywne wycieczki.
     * @return lista wszystkich aktywnych wycieczek
     */
    List<Trip> findByIsActiveTrue();
}
