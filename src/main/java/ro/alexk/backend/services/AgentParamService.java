package ro.alexk.backend.services;

import ro.alexk.backend.models.rest.AgentParamDTO;

import java.util.List;

public interface AgentParamService {
    List<AgentParamDTO> getAll();
    boolean updateValue(int id, String value);
}
