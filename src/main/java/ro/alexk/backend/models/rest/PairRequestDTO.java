package ro.alexk.backend.models.rest;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PairRequestDTO(int id, int blueprintId, String macAddr, String date) {
}
