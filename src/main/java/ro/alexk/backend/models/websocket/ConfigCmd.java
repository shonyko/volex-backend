package ro.alexk.backend.models.websocket;

import lombok.Builder;
import lombok.Getter;
import ro.alexk.backend.models.db.Config;

@Getter
@Builder
public class ConfigCmd {
    public static String CMD = "conf";

    private String mac;
    private Config config;
}
