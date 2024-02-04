package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexk.backend.models.rest.AgentDTO;
import ro.alexk.backend.services.AgentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agents")
public class AgentsController {
    private final AgentService agentService;

    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAll() {
        return ResponseEntity.ok(agentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDTO> getById(@PathVariable Integer id) {
        return agentService.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unlink(@PathVariable Integer id) {
        agentService.unlink(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/add-virtual")
    public ResponseEntity<AgentDTO> addVirtual(@RequestParam Integer blueprintId) {
        return agentService.createVirtualAgent(blueprintId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
