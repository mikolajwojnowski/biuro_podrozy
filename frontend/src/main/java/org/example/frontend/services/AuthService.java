package org.example.frontend.services;

import org.example.frontend.models.AuthToken;
import org.example.frontend.utils.ApiClient;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

/// AuthService is the service for the authentication.
/// It handles the login and registration processes.
/// It also handles the logout process.
/// It uses the ApiClient to make the requests to the backend.
/// It uses the AuthToken to store the authentication token.
/// It uses the JsonUtils to parse the JSON responses from the backend.
/// It uses the DialogUtils to show the error messages to the user.
/// It uses the AppContext to get the ApiClient.
/// It uses the UserService to get the user information.
/// It uses the TripService to get the trip information.
/// It uses the BookingService to get the booking information.
/// It uses the UserController to get the user information.
/// It uses the TripController to get the trip information.
/// It uses the BookingController to get the booking information.


public class AuthService {
    private final ApiClient apiClient;
    private static final String BASE_URL = "/auth";

    public AuthService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public AuthToken login(String email, String password) throws IOException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);
        return apiClient.post(BASE_URL + "/login", credentials, AuthToken.class);
    }

    public AuthToken register(String email, String password, String confirmPassword) throws IOException {
        Map<String, String> registrationData = new HashMap<>();
        registrationData.put("email", email);
        registrationData.put("password", password);
        registrationData.put("confirmPassword", confirmPassword);
        return apiClient.post(BASE_URL + "/register", registrationData, AuthToken.class);
    }

    public void logout() {
        apiClient.clearAuthToken();
    }

    public void changePassword(String oldPassword, String newPassword) throws IOException {
        Map<String, String> passwordData = new HashMap<>();
        passwordData.put("oldPassword", oldPassword);
        passwordData.put("newPassword", newPassword);
        
        try {
            apiClient.post(BASE_URL + "/change-password", passwordData, Void.class);
        } catch (Exception e) {
            // If the password was changed successfully but we got an empty response
            if (e.getMessage() != null && e.getMessage().contains("No content to map")) {
                return;
            }
            throw e;
        }
        logout();
    }
}