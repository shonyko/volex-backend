package ro.alexk.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ro.alexk.backend.entities.HwAgent;
import ro.alexk.backend.models.websocket.Message;
import ro.alexk.backend.models.websocket.Response;
import ro.alexk.backend.repositories.HwAgentRepository;
import ro.alexk.backend.services.SocketService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final SocketService socketService;

    @GetMapping
    public String test() {
        return "Hello World";
    }

    @GetMapping("/yes")
    public Mono<Response> s() {
        return socketService.sendMessage(
                Message.builder()
                        .to("test")
                        .data("test")
                        .build()
        ).onErrorResume(err ->
                Mono.just(new Response(false, err.getMessage(), null))
        );
    }
}
