package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.entities.Pin;
import ro.alexk.backend.models.db.Config;
import ro.alexk.backend.models.db.Input;
import ro.alexk.backend.models.db.Param;
import ro.alexk.backend.models.db.projections.config.PinConfig;
import ro.alexk.backend.models.db.projections.config.SrcPin;
import ro.alexk.backend.models.websocket.ConfigRequestEvent;
import ro.alexk.backend.repositories.AgentPinRepository;
import ro.alexk.backend.repositories.HwAgentRepository;
import ro.alexk.backend.services.ConfigRequestService;

import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigRequestServiceImpl implements ConfigRequestService {
    private final HwAgentRepository hwAgentRepository;
    private final AgentPinRepository agentPinRepository;

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public Config handleConfigRequest(ConfigRequestEvent cre) {
        var hwAgent = hwAgentRepository.findByMacAddr(cre.mac());

        var params = hwAgent.getAgent().getParams().stream()
                .map(p -> new Param(p.getId(), p.getValue()))
                .toList();

        var pins = hwAgent.getAgent().getPins().stream().collect(Collectors
                .partitioningBy(p -> Pin.PinType.OUT.equals(p.getPin().getType()))
        );

        var inputs = pins.get(false).stream().map(p -> {
            var srcPin = agentPinRepository.getSrcPinById(p.getId());
//            if(srcPin.isPresent()) {
//                System.out.println(p.getId());
//                System.out.println(srcPin.get().getId());
//                System.out.println(srcPin.get().getLastValue());
//            }
            var srcPinId = srcPin.map(SrcPin::getId).orElse(0);
            var value = srcPin.map(SrcPin::getLastValue).orElse(p.getLastValue());
            return new Input(p.getId(), srcPinId, value);
        }).toList();

        var outputs = pins.get(true).stream().map(PinConfig::getId).toList();

        return Config.builder()
                .id(hwAgent.getAgent().getId())
                .params(params)
                .inputs(inputs)
                .outputs(outputs)
                .build();
    }
}