package ro.alexk.backend.utils.option;

public record Some<T>(T value) implements Option<T> {
}
