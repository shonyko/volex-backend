package ro.alexk.backend.models.db.projections.config;

public interface PinConfig {
    int getId();
    BasePin getPin();
    String getLastValue();
}
