package ro.alexk.backend.utils.result;

public record Err<V>(Error err) implements Result<V> {
}
