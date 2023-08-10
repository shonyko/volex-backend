package ro.alexk.backend.models.db;

import lombok.Builder;

@Builder
public record WifiCredentials(
        String ssid,
        String pass
) {
}
