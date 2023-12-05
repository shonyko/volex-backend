package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexk.backend.models.db.WifiCredentials;
import ro.alexk.backend.services.WifiCredentialsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wifi-config")
public class WifiController {
    private final WifiCredentialsService service;

    @GetMapping
    public ResponseEntity<WifiCredentials> get() {
        return ResponseEntity.ok(service.getCredentials());
    }

    @PostMapping
    public ResponseEntity<Void> post(@RequestBody WifiCredentials credentials) {
        service.setCredentials(credentials);
        //TODO: update all clients via websockets
        return ResponseEntity.noContent().build();
    }
}
