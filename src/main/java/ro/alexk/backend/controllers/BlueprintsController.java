package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexk.backend.models.rest.BlueprintDTO;
import ro.alexk.backend.services.BlueprintService;
import ro.alexk.backend.utils.errors.Error;
import ro.alexk.backend.utils.result.Err;
import ro.alexk.backend.utils.result.Ok;

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
        return switch (blueprintService.delete(id)) {
            case Ok(var ignored) -> ResponseEntity.noContent().build();
            case Err(Error err) -> ResponseEntity.badRequest().body(err.getMessage());
        };
    }
}
