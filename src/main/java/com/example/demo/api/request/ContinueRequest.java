package com.example.demo.api.request;

import java.util.UUID;

public record ContinueRequest(
        UUID stepId,
        Object value
) {
}
