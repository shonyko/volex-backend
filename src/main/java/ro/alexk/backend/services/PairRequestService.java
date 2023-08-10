package ro.alexk.backend.services;

import ro.alexk.backend.models.db.WifiCredentials;
import ro.alexk.backend.models.websocket.PairRequestEvent;

import java.util.Optional;

public interface PairRequestService {
    Optional<WifiCredentials> handlePairRequestEvent(PairRequestEvent pre);
    boolean accept(Integer id);
}
