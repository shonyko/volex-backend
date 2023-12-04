package ro.alexk.backend.services;

import ro.alexk.backend.models.rest.AgentPinDTO;
import ro.alexk.backend.models.websocket.PinValueEvent;

import java.util.List;

public interface AgentPinService {
    void handlePinValueEvent(PinValueEvent e);

    List<AgentPinDTO> getAll();
}
