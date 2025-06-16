package org.example.frontend;

import org.example.frontend.models.AuthToken;
import org.example.frontend.utils.ApiClient;

/**
 * Manages global application state and context
 */
/// AppContext is responsible for managing the global application state and context
/// It uses the AuthToken to store the authentication token.
/// It uses the ApiClient to make the requests to the backend.
/// It uses the JsonUtils to parse the JSON responses from the backend.
/// It uses the DialogUtils to show the error messages to the user.
/// It uses the UserService to get the user information.
/// It uses the TripService to get the trip information.

public class AppContext {
    private static AuthToken authToken;
    private static ApiClient apiClient;
    private static boolean isAdmin = false;

    private AppContext() {} // Prevent instantiation

    /**
     * Initialize application context
     */
    public static void init() {
        clear(); // Reset any existing state
        apiClient = new ApiClient(); // Initialize API client
    }

    /**
     * Set the current authentication token
     */
    public static void setAuthToken(AuthToken token) {
        authToken = token;
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        apiClient.setAuthToken(token != null ? token.getAccessToken() : null);
        isAdmin = token != null && "ADMIN".equals(token.getRole());
        System.out.println("Token set in AppContext: " + (token != null ? 
            "Access: " + token.getAccessToken().substring(0, 20) + "..." : "null"));
    }

    /**
     * Get the current authentication token
     */
    public static AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Check if current user is admin
     */
    public static boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Get current user's email
     */
    public static String getCurrentUserEmail() {
        return authToken != null ? authToken.getEmail() : null;
    }

    /**
     * Clear the current context (logout)
     */
    public static void clear() {
        authToken = null;
        isAdmin = false;
        if (apiClient != null) {
            apiClient.setAuthToken(null);
        }
    }

    public static void clearAuthToken() {
        authToken = null;
        if (apiClient != null) {
            apiClient.setAuthToken(null);
        }
    }

    public static boolean isAuthenticated() {
        return authToken != null;
    }

    public static ApiClient getApiClient() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }
}