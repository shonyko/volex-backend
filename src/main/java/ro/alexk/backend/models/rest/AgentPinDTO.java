package ro.alexk.backend.models.rest;

import lombok.Builder;
import ro.alexk.backend.entities.DataType;
import ro.alexk.backend.entities.Pin;

@Builder
public record AgentPinDTO(int id, String name, Pin.PinType pinType, DataType.Name dataType, int blueprintId, int agentId, String value, Integer srcPinId) {
}
