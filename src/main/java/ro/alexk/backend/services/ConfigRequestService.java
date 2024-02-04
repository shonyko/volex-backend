package ro.alexk.backend.services;

import ro.alexk.backend.models.db.Config;
import ro.alexk.backend.models.db.VirtualAgent;
import ro.alexk.backend.models.websocket.ConfigRequestEvent;

import java.util.List;

public interface ConfigRequestService {
    Config handleConfigRequest(ConfigRequestEvent cre);

    List<VirtualAgent> getVMConfig();
}
