package org.example.frontend.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/// ApiClient is responsible for connecting frontend to backend 
/// It uses the HttpClient to make the requests to the backend.
/// It uses the ObjectMapper to parse the JSON responses from the backend.
/// It uses the AuthToken to store the authentication token.
/// It uses the JsonUtils to parse the JSON responses from the backend.
/// It uses the DialogUtils to show the error messages to the user.
/// It uses the AppContext to get the ApiClient.
/// It uses the UserService to get the user information.
/// It uses the TripService to get the trip information.
public class ApiClient {
    private static String baseUrl = "http://localhost:8080";
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private String authToken;

    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public void clearAuthToken() {
        this.authToken = null;
    }

    
    private HttpRequest.Builder createRequestBuilder(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + path))
            .header("Content-Type", "application/json");
        
        if (authToken != null && !authToken.isEmpty()) {
            builder.header("Authorization", "Bearer " + authToken);
        }
        
        return builder;
    }

    public <T> T post(String path, Object body, Class<T> responseType) throws IOException {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpRequest request = createRequestBuilder(path)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 400) {
                throw new IOException("HTTP error " + response.statusCode() + ": " + response.body());
            }
            
            return objectMapper.readValue(response.body(), responseType);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    public <T> List<T> getList(String path, Class<T> elementType) throws IOException {
        try {
            HttpRequest request = createRequestBuilder(path)
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 400) {
                throw new IOException("HTTP error " + response.statusCode() + ": " + response.body());
            }
            
            return objectMapper.readValue(
                response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, elementType)
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    public byte[] getBinary(String path) throws IOException {
        try {
            HttpRequest request = createRequestBuilder(path)
                .GET()
                .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            
            if (response.statusCode() >= 400) {
                throw new IOException("HTTP error " + response.statusCode());
            }
            
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    public <T> T get(String path, Class<T> responseType) throws IOException {
        try {
            HttpRequest request = createRequestBuilder(path)
                .GET()
                .build();

            if (responseType == byte[].class) {
                HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                if (response.statusCode() >= 400) {
                    throw new IOException("HTTP error " + response.statusCode());
                }
                return responseType.cast(response.body());
            }

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 400) {
                throw new IOException("HTTP error " + response.statusCode() + ": " + response.body());
            }
            
            return objectMapper.readValue(response.body(), responseType);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    public <T> T get(String path, TypeReference<T> typeReference) throws IOException {
        try {
            HttpRequest request = createRequestBuilder(path)
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 400) {
                throw new IOException("HTTP error " + response.statusCode() + ": " + response.body());
            }
            
            return objectMapper.readValue(response.body(), typeReference);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    public <T> T put(String path, Object body, Class<T> responseType) throws IOException {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            HttpRequest request = createRequestBuilder(path)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 400) {
                throw new IOException("HTTP error " + response.statusCode() + ": " + response.body());
            }
            
            return objectMapper.readValue(response.body(), responseType);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    public void delete(String path) throws IOException {
        try {
            HttpRequest request = createRequestBuilder(path)
                .DELETE()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 400) {
                throw new IOException("HTTP error " + response.statusCode() + ": " + response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}