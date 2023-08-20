package ro.alexk.backend.services;

import ro.alexk.backend.models.db.Config;
import ro.alexk.backend.models.websocket.ConfigRequestEvent;

public interface ConfigRequestService {
    Config handleConfigRequest(ConfigRequestEvent cre);
}
