package ro.alexk.backend.models.websocket;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PairAckCmd {
    public static String CMD = "pair_accept";

    private String mac;
    private String ssid;
    private String pass;
}
