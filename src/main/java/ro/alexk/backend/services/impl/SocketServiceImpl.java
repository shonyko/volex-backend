package ro.alexk.backend.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ro.alexk.backend.Constants.Events;
import ro.alexk.backend.Constants.Services;
import ro.alexk.backend.models.websocket.*;
import ro.alexk.backend.services.AgentPinService;
import ro.alexk.backend.services.ConfigRequestService;
import ro.alexk.backend.services.PairRequestService;
import ro.alexk.backend.services.SocketService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static ro.alexk.backend.utils.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final PairRequestService pairRequestService;
    private final ConfigRequestService configRequestService;
    private final AgentPinService agentPinService;

    private final List<String> subscriptions = new ArrayList<>();
    private final Socket socket = initSocket();
    void addEventHandler(Socket socket, String event, Emitter.Listener fn) {
        socket.on(event, fn);
        subscriptions.add(event);
    }

    private <T> Emitter.Listener createCallback(Consumer<T> consumer, Class<T> clazz) {
        return data -> {
            try {
                consumer.accept(deserialize(clazz, data));
            } catch (JsonProcessingException e) {
                log.error("Invalid json: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error: {}", e.getMessage());
            }
        };
    }

    private Socket initSocket() {
        var socket = IO.socket(URI.create("ws://192.168.0.220:3000")).connect();
        socket.on(Socket.EVENT_CONNECT_ERROR, err -> log.error("Error connecting: {}", err));
        socket.on(Socket.EVENT_CONNECT, none -> {
            log.info("Connected!");

            socket.emit(Events.Socket.REGISTER, new String[]{Services.BACKEND}, data -> {
                try {
                    onRegistered(deserialize(Response.class, data));
                } catch (JsonProcessingException e) {
                    log.error("Invalid json");
                }
            });
        });

        addEventHandler(socket, Events.PAIR, createCallback(this::onPairRequest, PairRequestEvent.class));
        addEventHandler(socket, Events.CONFIG, createCallback(this::onConfigRequest, ConfigRequestEvent.class));
        addEventHandler(socket, Events.PIN_VALUE, createCallback(this::onPinValue, PinValueEvent.class));

        return socket.connect();
    }

    public Mono<Response> sendMessage(Message msg) {
        return Mono.create(emitter ->
                socket.emit(Events.MESSAGE, new Object[]{toJsonObject(msg)}, data -> {
                    try {
                        emitter.success(deserialize(Response.class, data));
                    } catch (JsonProcessingException e) {
                        log.error("Invalid message response: ", e);
                        emitter.error(e);
                    }
                })
        );
    }

    private void onRegistered(Response res) {
        if (!res.success()) {
            log.error("Failed to register service: {}", res.err());
            return;
        }

        for (var sub : subscriptions) {
            socket.emit(Events.Socket.SUBSCRIBE, sub);
        }

        log.info("Service registered!");
    }

    private void onPairRequest(PairRequestEvent pr) {
        pairRequestService.handlePairRequestEvent(pr)
                .map(c -> PairAckCmd
                        .builder()
                        .mac(pr.mac())
                        .ssid(c.ssid())
                        .pass(c.pass())
                        .build()
                ).map(cmd -> Message
                        .builder()
                        .to(Services.SERIAL_LISTENER)
                        .cmd(PairAckCmd.CMD)
                        .data(toJson(cmd))
                        .build()
                ).ifPresent(msg -> {
                    System.out.println("sent " + msg);
                    socket.emit(Events.MESSAGE, toJsonObject(msg));
                });
    }

    private void onConfigRequest(ConfigRequestEvent cr) {
        var config = configRequestService.handleConfigRequest(cr);
        var cmd = ConfigCmd.builder()
                .mac(cr.mac())
                .config(toJson(config))
                .build();
        var msg = Message.builder()
                .to(Services.MQTT_BROKER)
                .cmd(ConfigCmd.CMD)
                .data(toJson(cmd))
                .build();
        System.out.println("sent " + msg);
        socket.emit(Events.MESSAGE, toJsonObject(msg));
    }

    private void onPinValue(PinValueEvent pv) {
        agentPinService.handlePinValueEvent(pv);
    }
}
