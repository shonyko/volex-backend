package ro.alexk.backend.models.db;

import lombok.Builder;

import java.util.List;

@Builder
public record Config(
        int id,
        List<Param> params,
        List<Input> inputs,
        List<Integer> outputs
) {
}
