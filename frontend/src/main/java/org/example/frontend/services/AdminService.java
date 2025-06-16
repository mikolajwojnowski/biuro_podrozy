package org.example.frontend.services;

import java.io.IOException;
import org.example.frontend.utils.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class AdminService {

    private ApiClient apiClient;
    private ObjectMapper objectMapper;

    public AdminService(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.objectMapper = new ObjectMapper();
    }

    public void deactivateUser(Long userId) throws IOException {
        apiClient.delete("/api/admin/users/" + userId);
    }

    public void promoteToAdmin(Long userId) throws IOException {
        try {
            apiClient.post("/api/admin/users/" + userId + "/promote", null, Void.class);
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("403")) {
                // Try to parse the error response for more details
                try {
                    String responseBody = errorMessage.substring(errorMessage.indexOf("{"));
                    Map<String, String> errorMap = objectMapper.readValue(responseBody, Map.class);
                    String detailedMessage = errorMap.get("message");
                    if (detailedMessage != null) {
                        throw new IOException("Access denied: " + detailedMessage);
                    }
                } catch (Exception parseError) {
                    // If we can't parse the error, throw the original error
                    throw new IOException("Access denied: You don't have permission to promote users");
                }
            }
            throw e;
        }
    }

    public void demoteToUser(Long userId) throws IOException {
        try {
            apiClient.post("/api/admin/users/" + userId + "/demote", null, Void.class);
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("403")) {
                // Try to parse the error response for more details
                try {
                    String responseBody = errorMessage.substring(errorMessage.indexOf("{"));
                    Map<String, String> errorMap = objectMapper.readValue(responseBody, Map.class);
                    String detailedMessage = errorMap.get("message");
                    if (detailedMessage != null) {
                        throw new IOException("Access denied: " + detailedMessage);
                    }
                } catch (Exception parseError) {
                    // If we can't parse the error, throw the original error
                    throw new IOException("Access denied: You don't have permission to demote users");
                }
            }
            throw e;
        }
    }
} 