package ro.alexk.backend.models.websocket;

import lombok.Builder;

@Builder
public record Message(
        String to,
        String cmd,
        String data
) {
}
