package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/{id}/value")
    public ResponseEntity<Void> updateValue(@PathVariable Integer id, @RequestBody String value) {
        if (!pinService.updateValue(id, value)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/src")
    public ResponseEntity<Void> updateSource(@PathVariable Integer id, @RequestBody Integer srcId) {
        if (!pinService.updateSrc(id, srcId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/src")
    public ResponseEntity<Void> unsetSource(@PathVariable Integer id) {
        if (!pinService.unsetSrc(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
