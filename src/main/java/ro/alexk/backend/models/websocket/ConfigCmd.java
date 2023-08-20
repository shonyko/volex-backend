package ro.alexk.backend.models.websocket;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfigCmd {
    public static String CMD = "conf";

    private String mac;
    private String config;
}
