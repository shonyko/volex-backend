package ro.alexk.backend.utils.result;

public record Ok<V, E extends Error>() implements Result<V, E> {
}
