package com.example.demo.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.*;

import java.util.*;

public class JsonUtil {

    private static final Logger log = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static <T> Optional<T> toObject(String json, Class<T> clazz) {
        try {
            T result = objectMapper.readValue(json, clazz);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json read value error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> T toObjectOrElseThrow(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Json read value error: {}", e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public static Optional<String> toJson(Object object) {
        try {
            String result = objectMapper.writeValueAsString(object);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json write value error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static String toJsonOrElseThrow(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Json write value error: {}", e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> Optional<T> toType(String json, TypeReference<T> typeReference) {
        try {
            T result = objectMapper.readValue(json, typeReference);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json type value error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> T toTypeOrElseThrow(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("Json type value error: {}", e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
}
