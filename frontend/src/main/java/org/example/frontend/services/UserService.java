package org.example.frontend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.frontend.models.UserDTO;
import org.example.frontend.utils.ApiClient;

import java.io.IOException;
import java.util.List;

public class UserService {
    private final ApiClient apiClient;
    private static final String BASE_URL = "/api/admin/users";

    public UserService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<UserDTO> getAllUsers() throws IOException {
        return apiClient.get(BASE_URL, new TypeReference<List<UserDTO>>() {});
    }

    public void deactivateUser(Long id) throws IOException {
        apiClient.delete(BASE_URL + "/deactivate/" + id);
    }
} 