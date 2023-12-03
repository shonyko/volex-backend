package ro.alexk.backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.alexk.backend.models.db.WifiCredentials;
import ro.alexk.backend.repositories.WifiCredentialsRepository;
import ro.alexk.backend.services.WifiCredentialsService;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class WifiCredentialsServiceImpl implements WifiCredentialsService {
    private final WifiCredentialsRepository repository;

    @Override
    public WifiCredentials getCredentials() {
        var ssid = repository.getSSID();
        var pass = repository.getPass();
        return WifiCredentials.builder()
                .ssid(ssid)
                .pass(pass)
                .build();
    }

    @Override
    public void setCredentials(WifiCredentials credentials) {
        repository.setSSID(credentials.ssid());
        repository.setPass(credentials.pass());
    }
}
