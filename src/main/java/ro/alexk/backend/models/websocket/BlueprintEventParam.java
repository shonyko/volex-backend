package ro.alexk.backend.models.websocket;

public record BlueprintEventParam(
        String name,
        String dataType,
        String defaultValue
) {
}
