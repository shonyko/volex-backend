package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexk.backend.models.rest.BlueprintDTO;
import ro.alexk.backend.services.BlueprintService;
import ro.alexk.backend.utils.result.Err;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprints")
public class BlueprintsController {
    private final BlueprintService blueprintService;

    @GetMapping
    public ResponseEntity<List<BlueprintDTO>> getAll() {
        return ResponseEntity.ok(blueprintService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        if(blueprintService.delete(id) instanceof Err<Void> e) {
            return ResponseEntity.badRequest().body(e.err().getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}
