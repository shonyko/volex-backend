package ro.alexk.backend.models.websocket;

public record BlueprintEventPin(
        String name,
        String dataType,
        String defaultValue
) {
}
