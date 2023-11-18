package ro.alexk.backend.services;

import ro.alexk.backend.models.db.WifiCredentials;

import java.util.Optional;

public interface WifiCredentialsService {
    WifiCredentials getCredentials();
    void setCredentials(WifiCredentials credentials);
}
