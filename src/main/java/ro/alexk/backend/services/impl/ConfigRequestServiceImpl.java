package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.entities.Pin;
import ro.alexk.backend.models.db.Config;
import ro.alexk.backend.models.db.Input;
import ro.alexk.backend.models.db.Param;
import ro.alexk.backend.models.db.VirtualAgent;
import ro.alexk.backend.models.db.projections.config.AgentConfig;
import ro.alexk.backend.models.db.projections.config.PinConfig;
import ro.alexk.backend.models.db.projections.config.SrcPin;
import ro.alexk.backend.models.websocket.ConfigRequestEvent;
import ro.alexk.backend.repositories.AgentPinRepository;
import ro.alexk.backend.repositories.AgentRepository;
import ro.alexk.backend.repositories.HwAgentRepository;
import ro.alexk.backend.services.ConfigRequestService;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigRequestServiceImpl implements ConfigRequestService {
    private final HwAgentRepository hwAgentRepository;
    private final AgentPinRepository agentPinRepository;
    private final AgentRepository agentRepository;

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public Config handleConfigRequest(ConfigRequestEvent cre) {
        AgentConfig config;
        if (cre.id() != null) {
            config = agentRepository.findConfigById(cre.id());
        } else config = hwAgentRepository.findByMacAddr(cre.mac()).getAgent();
        return buildConfig(config);
    }

    @Override
    public List<VirtualAgent> getVMConfig() {
        return agentRepository.getVirtualAgents();
    }

    private Config buildConfig(AgentConfig agent) {
        var params = agent.getParams().stream()
                .map(p -> new Param(p.getId(), p.getValue()))
                .sorted(Comparator.comparingInt(Param::id))
                .toList();

        var pins = agent.getPins().stream().collect(Collectors
                .partitioningBy(p -> Pin.PinType.OUT.equals(p.getPin().getType()))
        );

        var inputs = pins.get(false).stream().map(p -> {
            var srcPin = agentPinRepository.getSrcPinById(p.getId());
            var srcPinId = srcPin.map(SrcPin::getId).orElse(0);
            var value = srcPin.map(SrcPin::getLastValue).orElse(p.getLastValue());
            return new Input(p.getId(), srcPinId, value);
        }).toList();

        var outputs = pins.get(true).stream().map(PinConfig::getId).toList();

        return Config.builder()
                .id(agent.getId())
                .params(params)
                .inputs(inputs)
                .outputs(outputs)
                .build();
    }
}
