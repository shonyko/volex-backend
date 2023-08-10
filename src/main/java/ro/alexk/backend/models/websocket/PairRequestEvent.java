package ro.alexk.backend.models.websocket;

public record PairRequestEvent(
        String mac, String type
) {
}
