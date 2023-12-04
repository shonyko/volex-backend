package ro.alexk.backend.models.rest;

import lombok.Builder;

@Builder
public record AgentDTO(int id, String name, int blueprintId, String macAddr) {
}
