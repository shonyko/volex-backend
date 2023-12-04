package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.models.rest.AgentParamDTO;
import ro.alexk.backend.repositories.AgentParamsRepository;
import ro.alexk.backend.services.AgentParamService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentParamServiceImpl implements AgentParamService {
    private final AgentParamsRepository repository;

    @Override
    public List<AgentParamDTO> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public boolean updateValue(int id, String value) {
        // TODO: maybe add type check
        // TODO: send to mqtt
        return repository.updateValue(id, value) > 0;
    }
}
