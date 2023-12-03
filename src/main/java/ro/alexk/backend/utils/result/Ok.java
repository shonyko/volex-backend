package ro.alexk.backend.utils.result;

public record Ok<V>(V value) implements Result<V> {
}
