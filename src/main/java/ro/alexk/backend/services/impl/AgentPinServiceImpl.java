package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.models.websocket.PinValueEvent;
import ro.alexk.backend.repositories.AgentPinRepository;
import ro.alexk.backend.services.AgentPinService;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class AgentPinServiceImpl implements AgentPinService {
    private final AgentPinRepository agentPinRepository;

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void handlePinValueEvent(PinValueEvent e) {
        agentPinRepository.updatePinValue(e.id(), e.value());
    }
}
