package ro.alexk.backend.utils.result;

public sealed interface Result<V> permits Ok, Err {
}
