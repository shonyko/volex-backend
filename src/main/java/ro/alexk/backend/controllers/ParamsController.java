package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexk.backend.models.rest.AgentParamDTO;
import ro.alexk.backend.services.AgentParamService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/params")
public class ParamsController {
    private final AgentParamService paramService;

    @GetMapping
    public ResponseEntity<List<AgentParamDTO>> getAll() {
        return ResponseEntity.ok(paramService.getAll());
    }

    @PostMapping("/{id}/value")
    public ResponseEntity<String> set(@PathVariable Integer id, @RequestBody String value) {
        if (!paramService.updateValue(id, value)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
