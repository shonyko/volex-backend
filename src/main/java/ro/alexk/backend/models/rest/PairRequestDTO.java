package ro.alexk.backend.models.rest;

import lombok.Builder;

@Builder
public record PairRequestDTO(int id, int blueprintId, String macAddr, String date) {
}
