package ro.alexk.backend.services;

import ro.alexk.backend.entities.HwAgent;
import ro.alexk.backend.entities.PairRequest;
import ro.alexk.backend.models.rest.AgentDTO;

import java.util.List;
import java.util.Optional;

public interface AgentService {
    HwAgent createHardwareAgent(PairRequest pr);

    List<AgentDTO> getAll();

    Optional<AgentDTO> getById(Integer id);
}
