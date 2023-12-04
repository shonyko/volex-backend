package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.alexk.backend.entities.*;
import ro.alexk.backend.models.rest.AgentDTO;
import ro.alexk.backend.repositories.*;
import ro.alexk.backend.services.AgentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final AgentParamsRepository agentParamsRepository;
    private final AgentPinRepository agentPinRepository;
    private final HwAgentRepository hwAgentRepository;

    @Override
    public void createHardwareAgent(PairRequest pr) {
        var blueprint = pr.getBlueprint();

        var agent = agentRepository.save(Agent.builder()
                .blueprint(blueprint)
                .name(blueprint.getDisplayName())
                .build()
        );

        agentParamsRepository.saveAll(blueprint.getParams().stream()
                .map(p -> AgentParam.builder()
                        .param(p)
                        .agent(agent)
                        .value(p.getDefaultValue())
                        .build()
                ).toList()
        );

        agentPinRepository.saveAll(blueprint.getPins().stream()
                .map(pin -> AgentPin.builder()
                        .pin(pin)
                        .agent(agent)
                        .lastValue(pin.getDefaultValue())
                        .srcPin(null)
                        .build()
                ).toList()
        );

        var hwAgent = HwAgent.builder()
                .agent(agent)
                .macAddr(pr.getMacAddr())
                .build();

        hwAgentRepository.save(hwAgent);
    }

    @Override
    public List<AgentDTO> getAll() {
        return agentRepository
                .getAll().stream()
                .map(ap -> AgentDTO
                        .builder()
                        .id(ap.getId())
                        .name(ap.getName())
                        .blueprintId(ap.getBlueprintId())
                        .macAddr(ap.getMacAddr())
                        .build()
                )
                .toList();
    }
}
