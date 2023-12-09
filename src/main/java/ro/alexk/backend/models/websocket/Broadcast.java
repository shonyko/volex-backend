package ro.alexk.backend.models.websocket;

public record Broadcast<T>(String event, T data) {
}
