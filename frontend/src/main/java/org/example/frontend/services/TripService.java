package org.example.frontend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.frontend.models.Trip;
import org.example.frontend.utils.ApiClient;

import java.io.IOException;
import java.util.List;

/// TripService is the service for the trip.
/// It handles the trip information and the trip reservation.
/// It uses the ApiClient to make the requests to the backend.
/// It uses the Trip to store the trip information.
/// It uses the DialogUtils to show the error messages to the user.
/// It uses the AppContext to get the ApiClient.
/// It uses the UserService to get the user information.
public class TripService {
    private final ApiClient apiClient;
    /// BASE_URL is the base URL for the trip service.
    private static final String BASE_URL = "/api/trips";

    public TripService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<Trip> getAllTrips() throws IOException {
        System.out.println("Fetching trips from backend...");
        List<Trip> trips = apiClient.get(BASE_URL, new TypeReference<List<Trip>>() {});
        System.out.println("Response received from backend. Number of trips: " + (trips != null ? trips.size() : 0));
        return trips;
    }

    public Trip getTrip(Long id) throws IOException {
        return apiClient.get(BASE_URL + "/" + id, Trip.class);
    }

    public Trip createTrip(Trip trip) throws IOException {
        return apiClient.post(BASE_URL, trip, Trip.class);
    }

    public Trip updateTrip(Long id, Trip trip) throws IOException {
        return apiClient.put(BASE_URL + "/" + id, trip, Trip.class);
    }

    public void deleteTrip(Long id) throws IOException {
        if (id == null) {
            throw new IllegalArgumentException("Trip ID cannot be null");
        }
        apiClient.delete(BASE_URL + "/" + id);
    }

    public void reserveTrip(Long tripId) throws IOException {
        apiClient.post(BASE_URL + "/" + tripId + "/reserve", null, Void.class);
    }
} 