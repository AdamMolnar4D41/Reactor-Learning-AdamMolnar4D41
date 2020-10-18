package org.example.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ExampleHandler {

    public Mono<ServerResponse> handleOne(ServerRequest request) {
        return ServerResponse.ok()
                .header("Access-Control-Allow-Origin", "*")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("foo"));
    }

    public Mono<ServerResponse> handleTwo(ServerRequest request) {
        return ServerResponse.ok()
                .header("Access-Control-Allow-Origin", "*")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(Mono.just(request.pathVariable("id")), String.class));
    }
}
