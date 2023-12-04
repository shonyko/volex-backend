package ro.alexk.backend.models.rest;

import ro.alexk.backend.entities.DataType;

public record AgentParamDTO(int id, String name, int blueprintId, DataType.Name dataType, String value, int agentId) {
}
