package ro.alexk.backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ro.alexk.backend.Constants.Events;
import ro.alexk.backend.entities.Blueprint;
import ro.alexk.backend.entities.DataType;
import ro.alexk.backend.entities.Param;
import ro.alexk.backend.entities.Pin;
import ro.alexk.backend.entities.Pin.PinType;
import ro.alexk.backend.models.rest.BlueprintDTO;
import ro.alexk.backend.models.websocket.BlueprintEvent;
import ro.alexk.backend.repositories.*;
import ro.alexk.backend.services.BlueprintService;
import ro.alexk.backend.services.SocketService;
import ro.alexk.backend.utils.result.Result;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ro.alexk.backend.utils.Utils.err;
import static ro.alexk.backend.utils.Utils.ok;

@Service
@RequiredArgsConstructor
public class BlueprintServiceImpl implements BlueprintService {
    private final BlueprintRepository repository;
    private final PinRepository pinRepository;
    private final ParamRepository paramRepository;
    private final DataTypeRepository dataTypeRepository;

    @Setter
    private SocketService socketService;

    @Override
    @Transactional
    public void handleBlueprintEvent(BlueprintEvent be) {
        var blueprint = repository.save(
                Blueprint.builder()
                        .name(be.name())
                        .displayName(be.displayName())
                        .noInputPins(be.inputs().size())
                        .noOutputPins(be.outputs().size())
                        .noParams(be.params().size())
                        .isHardware(false)
                        .isValid(be.isValid())
                        .build()
        );

        var inputsStream = be.inputs().stream()
                .map(i -> Pin.builder()
                        .name(i.name())
                        .type(PinType.IN)
                        .dataType(DataType.builder().id(dataTypeRepository.getIdByName(i.dataType())).build())
                        .defaultValue(i.defaultValue())
                        .blueprint(blueprint)
                        .build()
                );

        var outputsStream = be.outputs().stream()
                .map(o -> Pin.builder()
                        .name(o.name())
                        .type(PinType.OUT)
                        .dataType(DataType.builder().id(dataTypeRepository.getIdByName(o.dataType())).build())
                        .defaultValue(o.defaultValue())
                        .blueprint(blueprint)
                        .build()
                );

        pinRepository.saveAll(Stream.concat(inputsStream, outputsStream).toList());

        paramRepository.saveAll(be.params().stream()
                .map(p -> Param.builder()
                        .name(p.name())
                        .dataType(DataType.builder().id(dataTypeRepository.getIdByName(p.dataType())).build())
                        .defaultValue(p.defaultValue())
                        .blueprint(blueprint)
                        .build()
                ).toList()
        );

        socketService.broadcast(Events.NEW_BLUEPRINT, new BlueprintDTO(blueprint.getId(), blueprint.getDisplayName(), blueprint.isHardware(), blueprint.isValid()));
    }

    @Override
    public List<BlueprintDTO> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public Result<Void> delete(int id) {
        if (repository.existsByIdAndIsHardware(id, true)) {
            return err("Can't delete hardware blueprints");
        }
        repository.deleteById(id);
        socketService.broadcast(Events.DEL_BLUEPRINT, id);
        return ok(null);
    }

    @Override
    public Optional<String> getNameById(int id) {
        return repository.getNameById(id);
    }
}
