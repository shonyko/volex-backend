package ro.alexk.backend.models.db.projections;

import java.time.LocalDateTime;

public interface PairRequestProj {
    int getId();
    int getBlueprintId();
    String getMacAddr();
    LocalDateTime getDate();
}
