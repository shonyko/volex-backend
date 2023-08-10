package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.alexk.backend.entities.*;
import ro.alexk.backend.repositories.AgentParamsRepository;
import ro.alexk.backend.repositories.AgentRepository;
import ro.alexk.backend.repositories.HwAgentRepository;
import ro.alexk.backend.repositories.PinRepository;
import ro.alexk.backend.services.AgentService;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final AgentParamsRepository agentParamsRepository;
    private final PinRepository pinRepository;
    private final AgentRepository agentRepository;
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

        var noPins = blueprint.getNoInputPins() + blueprint.getNoOutputPins();
        pinRepository.saveAll(IntStream
                .range(0, noPins)
                .mapToObj(idx -> Pin.builder()
                        .type(idx < blueprint.getNoInputPins() ? Pin.PinType.IN : Pin.PinType.OUT)
                        .agent(agent)
                        .defaultValue("0")
                        .lastValue("0")
                        .build()
                ).toList()
        );

        var hwAgent = HwAgent.builder()
                .agent(agent)
                .macAddr(pr.getMacAddr())
                .build();

        hwAgentRepository.save(hwAgent);
    }
}
