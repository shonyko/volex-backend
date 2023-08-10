package ro.alexk.backend.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ro.alexk.backend.Constants.Events;
import ro.alexk.backend.Constants.Services;
import ro.alexk.backend.models.websocket.Message;
import ro.alexk.backend.models.websocket.PairAckCmd;
import ro.alexk.backend.models.websocket.PairRequestEvent;
import ro.alexk.backend.models.websocket.Response;
import ro.alexk.backend.services.PairRequestService;
import ro.alexk.backend.services.SocketService;

import java.net.URI;

import static ro.alexk.backend.utils.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final PairRequestService pairRequestService;

    private final Socket socket = initSocket();

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

        log.info("Service registered!");

        socket.off(Events.PAIR);
        socket.on(Events.PAIR, data -> {
            try {
                onPairRequest(deserialize(PairRequestEvent.class, data));
            } catch (JsonProcessingException e) {
                log.error("Invalid json: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error: {}", e.getMessage());
            }
        });
        socket.emit(Events.Socket.SUBSCRIBE, Events.PAIR);
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




}
