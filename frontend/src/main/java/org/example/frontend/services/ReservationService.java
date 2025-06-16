package org.example.frontend.services;

import org.example.frontend.utils.ApiClient;
import org.example.frontend.models.ReservationDTO;
import org.example.frontend.models.Reservation;
import org.example.frontend.AppContext;
import java.io.IOException;
import java.util.List;

public class ReservationService {
    private final ApiClient apiClient;
    private static final String BASE_PATH = "/api/reservations";
    private static final String ADMIN_BASE_PATH = "/api/admin/reservations";

    public ReservationService(ApiClient apiClient) {
        this.apiClient = apiClient;
        String token = AppContext.getAuthToken() != null ? AppContext.getAuthToken().getAccessToken() : null;
        if (token != null) {
            this.apiClient.setAuthToken(token);
        }
    }

    public Reservation createReservation(ReservationDTO reservationDTO) throws IOException {
        return apiClient.post(BASE_PATH, reservationDTO, Reservation.class);
    }

    public List<ReservationDTO> getReservationsForTrip(Long tripId) throws IOException {
        return apiClient.getList(BASE_PATH + "/trip/" + tripId, ReservationDTO.class);
    }

    public void cancelReservation(Long id) throws IOException {
        apiClient.delete(BASE_PATH + "/" + id);
    }

    public byte[] generateReservationPdf(Long reservationId) throws IOException {
        return apiClient.get("/api/reservations/" + reservationId + "/pdf", byte[].class);
    }
} 