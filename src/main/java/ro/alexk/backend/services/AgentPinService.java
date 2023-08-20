package ro.alexk.backend.services;

import ro.alexk.backend.models.websocket.PinValueEvent;

public interface AgentPinService {
    void handlePinValueEvent(PinValueEvent e);
}
