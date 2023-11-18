package ro.alexk.backend.utils.result;

public sealed interface Result<V, E extends Error> permits Ok, Err {
}
