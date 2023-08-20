package ro.alexk.backend.models.websocket;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ro.alexk.backend.utils.PinValueDeserializer;

public record PinValueEvent(
        Integer id,
        @JsonDeserialize(using = PinValueDeserializer.class)
        String value
) {
}
