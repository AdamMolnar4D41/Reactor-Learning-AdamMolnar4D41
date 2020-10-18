package org.example.webflux.controller.filter;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Only works for router based endpoints
 *
 * You can't get the request body:
 * "java.lang.IllegalStateException: Only one connection receive subscriber allowed"
 */
public class ExampleHandlerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        if (request.pathVariable("id").equalsIgnoreCase("FORBIDDEN")) {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }

        return next.handle(request);
    }
}
