package org.example.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.backend.models.Trip;
import org.example.backend.models.Reservation;
import org.example.backend.repository.TripRepository;
import org.example.backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public TripService(TripRepository tripRepository, ReservationRepository reservationRepository) {
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findByIsActiveTrue();
    }

    public Trip getTrip(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
    }

    @Transactional
    public Trip createTrip(Trip trip) {
        trip.setAvailableSpots(trip.getCapacity()); // Initially, available spots equals capacity
        trip.setActive(true); // Ensure new trips are active by default
        return tripRepository.save(trip);
    }

    @Transactional
    public Trip updateTrip(Long id, Trip tripDetails) {
        Trip trip = getTrip(id);
        
        // Get current number of active reservations and their total participants
        int totalParticipants = reservationRepository.findByTripIdAndActive(id, true).stream()
                .mapToInt(Reservation::getNumberOfPeople)
                .sum();
        
        trip.setTitle(tripDetails.getTitle());
        trip.setDescription(tripDetails.getDescription());
        trip.setTripDate(tripDetails.getTripDate());
        trip.setDays(tripDetails.getDays());
        trip.setPrice(tripDetails.getPrice());
        trip.setActive(tripDetails.isActive());
        
        // Update capacity and recalculate available spots
        trip.setCapacity(tripDetails.getCapacity());
        trip.setAvailableSpots(tripDetails.getCapacity() - totalParticipants);
        
        return tripRepository.save(trip);
    }

    @Transactional
    public void deleteTrip(Long id) {
        Trip trip = getTrip(id);
        trip.setActive(false);
        tripRepository.save(trip);
    }
} 