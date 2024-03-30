package com.example.demo.util;

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

    public static Optional<String> toJson(Object object) {
        try {
            String result = objectMapper.writeValueAsString(object);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json write value error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> Optional<T> toCollection(String json, TypeReference<T> typeReference) {
        try {
            T result = objectMapper.readValue(json, typeReference);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Json read value error: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
