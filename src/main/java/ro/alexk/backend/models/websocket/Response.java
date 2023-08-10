package ro.alexk.backend.models.websocket;

public record Response(
        boolean success,
        String err,
        String data
) {
}
