package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.Constants.Events;
import ro.alexk.backend.entities.AgentPin;
import ro.alexk.backend.entities.PairRequest;
import ro.alexk.backend.models.db.WifiCredentials;
import ro.alexk.backend.models.rest.AgentDTO;
import ro.alexk.backend.models.rest.AgentParamDTO;
import ro.alexk.backend.models.rest.AgentPinDTO;
import ro.alexk.backend.models.rest.PairRequestDTO;
import ro.alexk.backend.models.websocket.PairRequestEvent;
import ro.alexk.backend.repositories.BlueprintRepository;
import ro.alexk.backend.repositories.HwAgentRepository;
import ro.alexk.backend.repositories.PairRequestRepository;
import ro.alexk.backend.services.AgentService;
import ro.alexk.backend.services.PairRequestService;
import ro.alexk.backend.services.SocketService;
import ro.alexk.backend.services.WifiCredentialsService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PairRequestServiceImpl implements PairRequestService {
    private final PairRequestRepository repository;
    private final BlueprintRepository blueprintRepository;
    private final HwAgentRepository hwAgentRepository;

    private final WifiCredentialsService wifiCredentialsService;
    private final AgentService agentService;

    @Setter
    private SocketService socketService;

    @Override
    public Optional<WifiCredentials> handlePairRequestEvent(PairRequestEvent pre) {
        if (hwAgentRepository.existsByMacAddr(pre.mac())) {
            return Optional.of(wifiCredentialsService.getCredentials());
        }
        if (!repository.existsByMacAddr(pre.mac())) {
            createNew(pre);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public boolean accept(Integer id) {
        var prs = repository.removeById(id);
        if (prs.isEmpty()) {
            return false;
        }
        var hwAgent = agentService.createHardwareAgent(prs.get(0));
        var agent = hwAgent.getAgent();
        agent.getParams().stream()
                .map(ap -> new AgentParamDTO(
                        ap.getId(),
                        ap.getParam().getName(),
                        ap.getParam().getBlueprint().getId(),
                        ap.getParam().getDataType().getName(),
                        ap.getValue(),
                        ap.getAgent().getId()
                )).forEach(dto -> socketService.broadcast(Events.NEW_PARAM, dto));
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
                )).forEach(dto -> socketService.broadcast(Events.NEW_PIN, dto));
        socketService.broadcast(
                Events.NEW_AGENT,
                new AgentDTO(agent.getId(), agent.getName(), agent.getBlueprint().getId(), hwAgent.getMacAddr())
        );
        socketService.broadcast(Events.DEL_PAIR_REQUEST, id);
        return true;
    }

    @Override
    public List<PairRequestDTO> getAll() {
        return repository
                .getAll().stream()
                .map(pr -> PairRequestDTO.builder()
                        .id(pr.getId())
                        .blueprintId(pr.getBlueprintId())
                        .macAddr(pr.getMacAddr())
                        .date(pr.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .build())
                .toList();
    }

    private void createNew(PairRequestEvent pre) {
        blueprintRepository
                .getByName(pre.type())
                .map(blueprintId -> blueprintRepository.getReferenceById(blueprintId.getId()))
                .map(blueprintReference -> PairRequest
                        .builder()
                        .macAddr(pre.mac())
                        .blueprint(blueprintReference)
                        .date(LocalDateTime.now())
                        .build()
                ).ifPresentOrElse((pairRequest) -> {
                    PairRequest saved = repository.save(pairRequest);
                    socketService.broadcast(Events.NEW_PAIR_REQUEST,
                            new PairRequestDTO(saved.getId(), saved.getBlueprint().getId(), saved.getMacAddr(), saved.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    );
                }, () -> log.error("Blueprint not found: {}", pre.type()));
    }
}
