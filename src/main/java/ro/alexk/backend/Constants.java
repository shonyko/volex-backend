package ro.alexk.backend;

public interface Constants {
    interface Events {
        String BROADCAST = "event";
        String MESSAGE = "msg";
        String PAIR = "pair";
        String BLUEPRINT = "blueprint";
        String CONFIG = "conf";
        String PARAM_VALUE = "param_value";
        String PIN_VALUE = "pin_value";
        String PIN_SOURCE = "pin_source";
        String NEW_PAIR_REQUEST = "new_pair_request";
        String DEL_PAIR_REQUEST = "del_pair_request";
        String NEW_PARAM = "new_param";
        String NEW_PIN = "new_pin";
        String NEW_AGENT = "new_agent";
        String NEW_BLUEPRINT = "new_blueprint";
        String DEL_BLUEPRINT = "del_blueprint";

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
