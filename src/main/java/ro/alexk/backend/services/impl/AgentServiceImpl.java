package ro.alexk.backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ro.alexk.backend.Constants;
import ro.alexk.backend.entities.*;
import ro.alexk.backend.models.rest.AgentDTO;
import ro.alexk.backend.models.rest.AgentParamDTO;
import ro.alexk.backend.models.rest.AgentPinDTO;
import ro.alexk.backend.repositories.*;
import ro.alexk.backend.services.AgentService;
import ro.alexk.backend.services.SocketService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final AgentParamsRepository agentParamsRepository;
    private final AgentPinRepository agentPinRepository;
    private final HwAgentRepository hwAgentRepository;
    private final BlueprintRepository blueprintRepository;

    @Setter
    private SocketService socketService;

    @Override
    public HwAgent createHardwareAgent(PairRequest pr) {
        var blueprint = pr.getBlueprint();
        var agent = createAgent(blueprint);
        var hwAgent = HwAgent.builder()
                .agent(agent)
                .macAddr(pr.getMacAddr())
                .build();

        return hwAgentRepository.save(hwAgent);
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

    @Override
    public Optional<AgentDTO> getById(Integer id) {
        return agentRepository.getDtoById(id);
    }

    @Override
    @Transactional
    public Optional<AgentDTO> createVirtualAgent(Integer blueprintId) {
        var blueprint = blueprintRepository.findById(blueprintId);
        if(blueprint.isEmpty()) {
            return Optional.empty();
        }

        var agent = createAgent(blueprint.get());
        var agentDto = getById(agent.getId());
        if(agentDto.isEmpty()) {
            return Optional.empty();
        }

        agent.getParams().stream()
                .map(ap -> new AgentParamDTO(
                        ap.getId(),
                        ap.getParam().getName(),
                        ap.getParam().getBlueprint().getId(),
                        ap.getParam().getDataType().getName(),
                        ap.getValue(),
                        ap.getAgent().getId()
                )).forEach(dto -> socketService.broadcast(Constants.Events.NEW_PARAM, dto));
        agent.getPins().stream()
                .map(ap -> new AgentPinDTO(
                        ap.getId(),
                        ap.getPin().getName(),
                        ap.getPin().getType(),
                        ap.getPin().getDataType().getName(),
                        ap.getPin().getBlueprint().getId(),
                        ap.getAgent().getId(),
                        ap.getLastValue(),
                        Optional.ofNullable(ap.getSrcPin()).map(AgentPin::getId).orElse(null)
                )).forEach(dto -> socketService.broadcast(Constants.Events.NEW_PIN, dto));
        socketService.broadcast(Constants.Events.NEW_AGENT, agentDto.get());

        return agentDto;
    }

    @Override
    public void unlink(Integer id) {
        // TODO: maybe restrict it only for software agents as there is no way to block access to the network for hardware ones
        agentRepository.deleteById(id);
        // TODO: maybe include removing it from the VM server as a transaction step
        socketService.broadcast(Constants.Events.DEL_AGENT, id);
    }

    private Agent createAgent(Blueprint blueprint) {
        var agent = agentRepository.save(Agent.builder()
                .blueprint(blueprint)
                .name(blueprint.getDisplayName())
                .build()
        );

        var params = agentParamsRepository.saveAll(blueprint.getParams().stream()
                .map(p -> AgentParam.builder()
                        .param(p)
                        .agent(agent)
                        .value(p.getDefaultValue())
                        .build()
                ).toList()
        );
        agent.setParams(params);

        var pins = agentPinRepository.saveAll(blueprint.getPins().stream()
                .map(pin -> AgentPin.builder()
                        .pin(pin)
                        .agent(agent)
                        .lastValue(pin.getDefaultValue())
                        .srcPin(null)
                        .build()
                ).toList()
        );
        agent.setPins(pins);

        return agent;
    }
}
