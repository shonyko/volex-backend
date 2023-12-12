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
import ro.alexk.backend.services.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static ro.alexk.backend.utils.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final PairRequestService pairRequestService;
    private final ConfigRequestService configRequestService;
    private final AgentPinService agentPinService;
    private final BlueprintService blueprintService;
    private final WebSocketConfig webSocketConfig;

    private final List<String> subscriptions = new ArrayList<>();
    private Socket socket;

    void addEventHandler(Socket socket, String event, Emitter.Listener fn) {
        socket.on(event, fn);
        subscriptions.add(event);
    }

    private <T> Emitter.Listener createCallback(BiConsumer<T, Optional<Ack>> consumer, Class<T> clazz) {
        return data -> {
            Ack ack;
            if (data[data.length - 1] instanceof Ack a) {
                ack = a;
            } else {
                ack = null;
            }
            okOrElse(deserialize(clazz, data),
                    val -> consumer.accept(val, Optional.ofNullable(ack)),
                    err -> log.error("Could not deserialize data: ", err)
            );
        };
    }

    @PostConstruct
    private void initSocket() {
        String connString = String.format("ws://%s:%s", webSocketConfig.addr(), webSocketConfig.port());
        socket = IO.socket(URI.create(connString));
        socket.on(Socket.EVENT_CONNECT_ERROR, err -> log.error("Error connecting: ", err));
        socket.on(Socket.EVENT_CONNECT, none -> {
            log.info("Connected!");

            socket.emit(Events.Socket.REGISTER, Services.BACKEND,
                    (Ack) data -> okOrElse(deserialize(Response.class, data),
                            this::onRegistered,
                            err -> log.error("Could not deserialize data: ", err)
                    )
            );
        });

        addEventHandler(socket, Events.PAIR, createCallback(this::onPairRequest, PairRequestEvent.class));
        addEventHandler(socket, Events.CONFIG, createCallback(this::onConfigRequest, ConfigRequestEvent.class));
        addEventHandler(socket, Events.PIN_VALUE, createCallback(this::onPinValue, PinValueEvent.class));
        addEventHandler(socket, Events.BLUEPRINT, createCallback(this::onBlueprintEvent, BlueprintEvent.class));
        addEventHandler(socket, "test", createCallback(this::test, Response.class));

        socket.connect();

        agentPinService.setSocketService(this);
        pairRequestService.setSocketService(this);
        blueprintService.setSocketService(this);
    }

    private <T> void emit(Message<T> msg, Ack ack) {
        okOrElse(toJsonObject(msg),
                jsonObj -> {
                    socket.emit(Events.MESSAGE, jsonObj, ack);
                    log.info("Sent {}", msg);
                },
                err -> log.error("Could not serialize message: ", err)
        );
    }

    private <T> void emit(Message<T> msg) {
        emit(msg, null);
    }

    public Mono<Response> sendMessage(Message<?> msg) {
        return Mono.create(emitter ->
                emit(msg, new AckWithTimeout(2000) {
                    @Override
                    public void onSuccess(Object... data) {
                        okOrElse(deserialize(Response.class, data),
                                emitter::success,
                                err -> {
                                    log.error("Could not deserialize data: ", err);
                                    emitter.error(err.getCause());
                                }
                        );
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
        okOrElse(toJsonObject(new Broadcast<>(event, obj)),
                jsonObj -> {
                    socket.emit(Events.BROADCAST, jsonObj);
                    log.info("Sent {}", jsonObj);
                },
                err -> log.error("Could not serialize broadcast object: ", err)
        );
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

    private void onPairRequest(PairRequestEvent pr, Optional<Ack> ack) {
        pairRequestService.handlePairRequestEvent(pr)
                .map(wifiCredentials -> PairAckCmd
                        .builder()
                        .mac(pr.mac())
                        .ssid(wifiCredentials.ssid())
                        .pass(wifiCredentials.pass())
                        .build()
                ).map(cmd -> Message
                        .builder()
                        .to(Services.SERIAL_LISTENER)
                        .event(PairAckCmd.CMD)
                        .data(cmd)
                        .build()
                ).ifPresent(this::emit);
//                    ack.ifPresent(a -> {
//                        switch (toJsonObject(new Response(true, null, null))) {
//                            case Ok(var jsonObj) -> a.call(jsonObj);
//                            case Err(Error err) -> log.error("Could not serialize pair request ack response: ", err);
//                        }
//                    });
//                }, () -> ack.ifPresent(a -> {
//                    switch (toJsonObject(new Response(false, "Request still pending", null))) {
//                        case Ok(var jsonObj) -> a.call(jsonObj);
//                        case Err(Error err) -> log.error("Could not serialize pair request ack response: ", err);
//                    }
//                }));
    }

    private void onConfigRequest(ConfigRequestEvent cr, Optional<Ack> ack) {
        var config = configRequestService.handleConfigRequest(cr);
        Optional.of(config)
                .map(cfg -> ConfigCmd.builder()
                        .mac(cr.mac())
                        .config(cfg)
                        .build()
                ).map(cmd -> Message.builder()
                        .to(Services.MQTT_BROKER)
                        .event(ConfigCmd.CMD)
                        .data(cmd)
                        .build()
                ).ifPresent(this::emit);
    }

    private void onPinValue(PinValueEvent pv, Optional<Ack> ack) {
        agentPinService.handlePinValueEvent(pv);
    }

    private void onBlueprintEvent(BlueprintEvent be, Optional<Ack> ack) {
        blueprintService.handleBlueprintEvent(be);
        ack.ifPresent(a ->
                okOrElse(toJsonObject(new Response(true, null, null)),
                        a::call,
                        err -> log.error("Could not serialize blueprint event ack response: ", err)
                )
        );
    }

    private void test(Response res, Optional<Ack> ack) {
        ack.ifPresent(a -> okOrElse(toJsonObject(res),
                a::call,
                err -> log.error("Could not serialize pair request ack response: ", err)
        ));
    }
}
