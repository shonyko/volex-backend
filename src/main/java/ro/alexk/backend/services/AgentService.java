package ro.alexk.backend.services;

import ro.alexk.backend.entities.PairRequest;
import ro.alexk.backend.models.rest.AgentDTO;

import java.util.List;

public interface AgentService {
    void createHardwareAgent(PairRequest pr);
    List<AgentDTO> getAll();
}
