package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.Constants.Events;
import ro.alexk.backend.models.db.Param;
import ro.alexk.backend.models.rest.AgentParamDTO;
import ro.alexk.backend.repositories.AgentParamsRepository;
import ro.alexk.backend.services.AgentParamService;
import ro.alexk.backend.services.SocketService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentParamServiceImpl implements AgentParamService {
    private final AgentParamsRepository repository;
    private final SocketService socketService;

    @Override
    public List<AgentParamDTO> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public boolean updateValue(int id, String value) {
        // TODO: maybe add type check
        boolean success = repository.updateValue(id, value) > 0;
        if (success) {
            socketService.broadcast(Events.PARAM_VALUE, new Param(id, value));
        }
        return success;
    }
}
