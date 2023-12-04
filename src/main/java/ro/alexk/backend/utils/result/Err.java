package ro.alexk.backend.utils.result;

import ro.alexk.backend.utils.errors.Error;

public record Err<V>(Error err) implements Result<V> {
}
