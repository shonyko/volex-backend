package ro.alexk.backend.services;

import ro.alexk.backend.models.rest.AgentPinDTO;
import ro.alexk.backend.models.websocket.PinValueEvent;

import java.util.List;

public interface AgentPinService {
    void setSocketService(SocketService socketService);

    void handlePinValueEvent(PinValueEvent e);

    boolean updateValue(int id, String value);

    boolean updateSrc(int id, int srcId);

    boolean unsetSrc(int id);

    List<AgentPinDTO> getAll();
}
