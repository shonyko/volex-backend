package ro.alexk.backend.models.db.projections.config;

import ro.alexk.backend.entities.Pin;

public interface BasePin {
    Pin.PinType getType();
}
