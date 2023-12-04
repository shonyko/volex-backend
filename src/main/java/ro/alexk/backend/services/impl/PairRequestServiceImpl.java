package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.entities.PairRequest;
import ro.alexk.backend.models.db.WifiCredentials;
import ro.alexk.backend.models.rest.PairRequestDTO;
import ro.alexk.backend.models.websocket.PairRequestEvent;
import ro.alexk.backend.repositories.BlueprintRepository;
import ro.alexk.backend.repositories.HwAgentRepository;
import ro.alexk.backend.repositories.PairRequestRepository;
import ro.alexk.backend.services.AgentService;
import ro.alexk.backend.services.PairRequestService;
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

        agentService.createHardwareAgent(prs.get(0));
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
                ).ifPresentOrElse(repository::save, () -> log.error("Blueprint not found: {}", pre.type()));
    }
}
