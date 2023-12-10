package ro.alexk.backend.models.websocket;

import lombok.Builder;

@Builder
public record Message<T>(
        String to,
        String event,
        T data
) {
}
