package ro.alexk.backend.models.websocket;

import java.util.List;

public record BlueprintEvent(
        String name,
        String displayName,
        List<BlueprintEventPin> inputs,
        List<BlueprintEventPin> outputs,
        List<BlueprintEventParam> params,
        Boolean isValid
) {
}
