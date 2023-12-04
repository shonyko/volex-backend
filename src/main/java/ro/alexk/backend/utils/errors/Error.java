package ro.alexk.backend.utils.errors;

public class Error extends Exception {
    public Error(String msg) {
        this(msg, null);
    }

    public Error(Throwable cause) {
        this((cause == null ? null : cause.toString()), cause);
    }

    public Error(String msg, Throwable cause) {
        super(msg, cause, false, false);
    }
}
