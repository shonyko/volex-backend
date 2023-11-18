package ro.alexk.backend.utils.result;

public record Err<V, E extends Error>() implements Result<V, E> {
}
