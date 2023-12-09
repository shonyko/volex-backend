package ro.alexk.backend.services;

import ro.alexk.backend.models.db.WifiCredentials;
import ro.alexk.backend.models.rest.PairRequestDTO;
import ro.alexk.backend.models.websocket.PairRequestEvent;

import java.util.List;
import java.util.Optional;

public interface PairRequestService {
    void setSocketService(SocketService socketService);

    Optional<WifiCredentials> handlePairRequestEvent(PairRequestEvent pre);

    boolean accept(Integer id);

    List<PairRequestDTO> getAll();
}
