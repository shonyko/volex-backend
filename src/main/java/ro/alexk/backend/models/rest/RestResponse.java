package ro.alexk.backend.models.rest;

public record RestResponse(
        boolean success,
        String err
) {
}
