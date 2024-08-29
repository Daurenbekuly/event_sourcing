package kz.sashok.api.request;

import java.util.UUID;

public record ContinueRequest(
        UUID stepId,
        Object value
) {
}
