package ro.alexk.backend.models.db;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record WifiCredentials(
        @NotNull
        String ssid,
        @NotNull
        String pass
) {
}
