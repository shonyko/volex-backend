package ro.alexk.backend;

public interface Constants {
    interface Events {
        String MESSAGE = "msg";
        String PAIR = "pair";
        String CONFIG = "conf";
        String PIN_VALUE = "pin_value";

        interface Socket {
            String REGISTER = "register";
            String SUBSCRIBE = "subscribe";
            String OPEN = "open";
            String CLOSE = "close";
        }
    }

    interface Services {
        String BACKEND = "backend";
        String FRONTEND = "frontend";
        String MQTT_BROKER = "mqtt";
        String SERIAL_LISTENER = "serial-listener";
    }
}
