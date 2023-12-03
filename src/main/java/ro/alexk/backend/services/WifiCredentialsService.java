package ro.alexk.backend.services;

import ro.alexk.backend.models.db.WifiCredentials;

public interface WifiCredentialsService {
    WifiCredentials getCredentials();
    void setCredentials(WifiCredentials credentials);
}
