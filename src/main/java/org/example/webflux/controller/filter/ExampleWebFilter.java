package org.example.webflux.controller.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * This works for all endpoints (both annotation based and functional style based ones)
 */
@Component
public class ExampleWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        serverWebExchange.getResponse().getHeaders().add("filter-name", "example-web-filter");
        return webFilterChain.filter(serverWebExchange);
    }
}
