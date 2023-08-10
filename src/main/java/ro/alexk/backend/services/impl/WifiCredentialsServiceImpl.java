package ro.alexk.backend.services.impl;

import org.springframework.stereotype.Service;
import ro.alexk.backend.models.db.WifiCredentials;
import ro.alexk.backend.services.WifiCredentialsService;

import java.util.Optional;

@Service
public class WifiCredentialsServiceImpl implements WifiCredentialsService {
    @Override
    public Optional<WifiCredentials> getCredentials() {
        return Optional.of(
                WifiCredentials.builder()
                .ssid("UPC8894AD1")
                .pass("erBBjuh7cbnc")
                .build()
        );
    }

    @Override
    public boolean hasCredentials() {
        return true;
    }
}
