package ro.alexk.backend.models.db.projections.config;

import java.util.List;

public interface AgentConfig {
    int getId();
    List<ParamConfig> getParams();
    List<PinConfig> getPins();
}
