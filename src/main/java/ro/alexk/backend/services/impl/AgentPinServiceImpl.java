package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.Constants.Events;
import ro.alexk.backend.models.db.Input;
import ro.alexk.backend.models.db.Param;
import ro.alexk.backend.models.rest.AgentPinDTO;
import ro.alexk.backend.models.websocket.PinValueEvent;
import ro.alexk.backend.repositories.AgentPinRepository;
import ro.alexk.backend.services.AgentPinService;
import ro.alexk.backend.services.SocketService;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentPinServiceImpl implements AgentPinService {
    private final AgentPinRepository agentPinRepository;
    @Setter
    private SocketService socketService;

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void handlePinValueEvent(PinValueEvent e) {
        agentPinRepository.updatePinValue(e.id(), e.value());
    }

    @Override
    @Transactional
    public boolean updateValue(int id, String value) {
        // TODO: maybe check for ids to exist
        if (agentPinRepository.updatePinValue(id, value) <= 0) {
            return false;
        }
        socketService.broadcast(Events.PIN_VALUE, new Param(id, value));
        return true;
    }

    @Override
    @Transactional
    public boolean updateSrc(int id, int srcId) {
        // TODO: maybe check for ids to exist
        if (agentPinRepository.updatePinSource(id, srcId) <= 0) {
            return false;
        }
        String value = agentPinRepository.getValueById(srcId);
        socketService.broadcast(Events.PIN_SOURCE, new Input(id, srcId, value));
        return true;
    }

    @Override
    @Transactional
    public boolean unsetSrc(int id) {
        if(agentPinRepository.unsetPinSource(id) <= 0) {
            return false;
        }
        socketService.broadcast(Events.PIN_SOURCE, new Input(id, 0, ""));
        return true;
    }

    @Override
    public List<AgentPinDTO> getAll() {
        return agentPinRepository
                .getAll().stream()
                .map(ap -> AgentPinDTO.builder()
                        .id(ap.getId())
                        .name(ap.getName())
                        .pinType(ap.getPinType())
                        .dataType(ap.getDataType())
                        .blueprintId(ap.getBlueprintId())
                        .agentId(ap.getAgentId())
                        .value(ap.getValue())
                        .srcPinId(ap.getSrcPinId())
                        .build()
                )
                .toList();
    }
}
