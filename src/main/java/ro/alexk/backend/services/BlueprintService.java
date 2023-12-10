package ro.alexk.backend.services;


import ro.alexk.backend.models.rest.BlueprintDTO;
import ro.alexk.backend.models.websocket.BlueprintEvent;
import ro.alexk.backend.utils.result.Result;

import java.util.List;

public interface BlueprintService {
    void setSocketService(SocketService socketService);

    void handleBlueprintEvent(BlueprintEvent be);

    List<BlueprintDTO> getAll();

    Result<Void> delete(int id);
}
