package ro.alexk.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ws")
public record WebSocketConfig(String addr, String port) {
}
