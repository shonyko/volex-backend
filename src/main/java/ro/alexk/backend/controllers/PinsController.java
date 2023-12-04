package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.alexk.backend.models.rest.AgentPinDTO;
import ro.alexk.backend.services.AgentPinService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pins")
public class PinsController {
    private final AgentPinService pinService;

    @GetMapping
    public ResponseEntity<List<AgentPinDTO>> getAll() {
        return ResponseEntity.ok(pinService.getAll());
    }
}
