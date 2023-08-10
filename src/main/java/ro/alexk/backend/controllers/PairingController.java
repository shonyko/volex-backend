package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexk.backend.models.rest.RestResponse;
import ro.alexk.backend.services.PairRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pair")
public class PairingController {
    private final PairRequestService pairRequestService;

    @PostMapping("/{id}/accept")
    public ResponseEntity<RestResponse> set(@PathVariable Integer id) {
        try {
            var accepted = pairRequestService.accept(id);
            var err = accepted ? null : "Id not found";
            return ResponseEntity.ok(new RestResponse(accepted, err));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RestResponse(false, e.getMessage()));
        }
    }
}
