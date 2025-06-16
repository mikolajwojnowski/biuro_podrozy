package org.example.frontend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    // Serialize object to JSON string
    public static String toJson(Object object) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize object: " + object);
            throw e;
        }
    }

    // Deserialize JSON string to object
    public static <T> T fromJson(String json, Class<T> valueType) throws JsonProcessingException {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse JSON: " + json);
            throw e;
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeRef) throws JsonProcessingException {
        return objectMapper.readValue(json, typeRef);
    }
}