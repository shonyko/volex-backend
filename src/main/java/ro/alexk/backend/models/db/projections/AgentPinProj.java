package ro.alexk.backend.models.db.projections;

import ro.alexk.backend.entities.DataType;
import ro.alexk.backend.entities.Pin;

public interface AgentPinProj {
    int getId();
    String getName();
    Pin.PinType getPinType();
    DataType.Name getDataType();
    int getBlueprintId();
    int getAgentId();
    String getValue();
    Integer getSrcPinId();
}
