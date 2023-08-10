package ro.alexk.backend.services;

import ro.alexk.backend.entities.PairRequest;

public interface AgentService {
    void createHardwareAgent(PairRequest pr);
}
