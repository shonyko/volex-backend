package ro.alexk.backend.services;

import reactor.core.publisher.Mono;
import ro.alexk.backend.models.websocket.Message;
import ro.alexk.backend.models.websocket.Response;

public interface SocketService {

    Mono<Response> sendMessage(Message msg);
    <T> void broadcast(String event, T obj);
}
