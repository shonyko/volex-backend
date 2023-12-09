package ro.alexk.backend.services.impl;

import io.socket.client.Ack;
import io.socket.client.AckWithTimeout;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ro.alexk.backend.Constants.Events;
import ro.alexk.backend.Constants.Services;
import ro.alexk.backend.config.WebSocketConfig;
import ro.alexk.backend.models.websocket.*;
import ro.alexk.backend.services.AgentPinService;
import ro.alexk.backend.services.ConfigRequestService;
import ro.alexk.backend.services.PairRequestService;
import ro.alexk.backend.services.SocketService;
import ro.alexk.backend.utils.errors.Error;
import ro.alexk.backend.utils.result.Err;
import ro.alexk.backend.utils.result.Ok;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static ro.alexk.backend.utils.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final PairRequestService pairRequestService;
    private final ConfigRequestService configRequestService;
    private final AgentPinService agentPinService;
    private final WebSocketConfig webSocketConfig;

    private final List<String> subscriptions = new ArrayList<>();
    private Socket socket;

    void addEventHandler(Socket socket, String event, Emitter.Listener fn) {
        socket.on(event, fn);
        subscriptions.add(event);
    }

    private <T> Emitter.Listener createCallback(Consumer<T> consumer, Class<T> clazz) {
        return data -> {
            switch (deserialize(clazz, data)) {
                case Ok(T val) -> consumer.accept(val);
                case Err(Error err) -> log.error("Could not deserialize data: ", err);
            }
        };
    }

    @PostConstruct
    private void initSocket() {
        String connString = String.format("ws://%s:%s", webSocketConfig.addr(), webSocketConfig.port());
        socket = IO.socket(URI.create(connString));
        socket.on(Socket.EVENT_CONNECT_ERROR, err -> log.error("Error connecting: ", err));
        socket.on(Socket.EVENT_CONNECT, none -> {
            log.info("Connected!");

            socket.emit(Events.Socket.REGISTER, Services.BACKEND, (Ack) data -> {
                switch (deserialize(Response.class, data)) {
                    case Ok(Response res) -> onRegistered(res);
                    case Err(Error err) -> log.error("Could not deserialize data: ", err);
                }
            });
        });

        addEventHandler(socket, Events.PAIR, createCallback(this::onPairRequest, PairRequestEvent.class));
        addEventHandler(socket, Events.CONFIG, createCallback(this::onConfigRequest, ConfigRequestEvent.class));
        addEventHandler(socket, Events.PIN_VALUE, createCallback(this::onPinValue, PinValueEvent.class));

        socket.connect();

        agentPinService.setSocketService(this);
        pairRequestService.setSocketService(this);
    }

    private void emit(String event, Message msg, Ack ack) {
        switch (toJsonObject(msg)) {
            case Ok(var jsonObj) -> {
                socket.emit(event, jsonObj, ack);
                log.info("Sent {}", msg);
            }
            case Err(Error err) -> log.error("Could not serialize message: ", err);
        }
    }

    private void emit(String event, Message msg) {
        emit(event, msg, null);
    }

    public Mono<Response> sendMessage(Message msg) {
        return Mono.create(emitter ->
                emit(Events.MESSAGE, msg, new AckWithTimeout(2000) {
                    @Override
                    public void onSuccess(Object... data) {
                        switch (deserialize(Response.class, data)) {
                            case Ok(Response res) -> emitter.success(res);
                            case Err(Error err) -> {
                                log.error("Could not deserialize data: ", err);
                                emitter.error(err.getCause());
                            }
                        }
                    }

                    @Override
                    public void onTimeout() {
                        emitter.success(new Response(false, "request timeout", null));
                    }
                })
        );
    }

    public <T> void broadcast(String event, T obj) {
//        switch (toJson(obj)) {
//            case Ok(String data) -> {
                switch (toJsonObject(new Broadcast<>(event, obj))) {
                    case Ok(var jsonObj) -> {
                        socket.emit(Events.BROADCAST, jsonObj);
                        log.info("Sent {}", jsonObj);
                    }
                    case Err(Error err) -> log.error("Could not serialize broadcast object: ", err);
                }
//            }
//            case Err(Error err) -> log.error("Could not serialize data for broadcast: ", err);
//        }
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
                .map(wifiCredentials -> PairAckCmd
                        .builder()
                        .mac(pr.mac())
                        .ssid(wifiCredentials.ssid())
                        .pass(wifiCredentials.pass())
                        .build()
                ).map(cmd ->
                        switch (toJson(cmd)) {
                            case Ok(String json) -> Message
                                    .builder()
                                    .to(Services.SERIAL_LISTENER)
                                    .cmd(PairAckCmd.CMD)
                                    .data(json)
                                    .build();
                            case Err(Error err) -> {
                                log.error("Could not serialize command: ", err);
                                yield null;
                            }
                        }
                ).ifPresent(msg -> emit(Events.MESSAGE, msg));
    }

    private void onConfigRequest(ConfigRequestEvent cr) {
        var config = configRequestService.handleConfigRequest(cr);
        Optional.of(config)
                .map(cfg -> switch (toJson(cfg)) {
                    case Ok(String json) -> ConfigCmd.builder()
                            .mac(cr.mac())
                            .config(json)
                            .build();
                    case Err(Error err) -> {
                        log.error("Could not serialize configuration: ", err);
                        yield null;
                    }
                }).map(cmd -> switch (toJson(cmd)) {
                    case Ok(String json) -> Message.builder()
                            .to(Services.MQTT_BROKER)
                            .cmd(ConfigCmd.CMD)
                            .data(json)
                            .build();
                    case Err(Error err) -> {
                        log.error("Could not serialize command: ", err);
                        yield null;
                    }
                }).ifPresent(msg -> emit(Events.MESSAGE, msg));
    }

    private void onPinValue(PinValueEvent pv) {
        agentPinService.handlePinValueEvent(pv);
    }
}
