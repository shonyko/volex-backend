package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexk.backend.models.rest.PairRequestDTO;
import ro.alexk.backend.services.PairRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestsController {
    private final PairRequestService pairRequestService;

    @GetMapping
    public ResponseEntity<List<PairRequestDTO>> getAll() {
        return ResponseEntity.ok(pairRequestService.getAll());
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<String> set(@PathVariable Integer id) {
        try {
            if (!pairRequestService.accept(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
