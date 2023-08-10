package ro.alexk.backend.services;

import ro.alexk.backend.models.db.WifiCredentials;

import java.util.Optional;

public interface WifiCredentialsService {
    Optional<WifiCredentials> getCredentials();
    boolean hasCredentials();
}
