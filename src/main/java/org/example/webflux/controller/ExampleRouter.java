package org.example.webflux.controller;

import org.example.webflux.controller.filter.ExampleHandlerFilterFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class ExampleRouter {

    @Autowired
    private ExampleHandler exampleHandler;

    @Bean
    public RouterFunction<ServerResponse> routeExample() {
        return RouterFunctions
                .route(RequestPredicates.GET("/example"), exampleHandler::handleOne);
    }

    @Bean
    public RouterFunction<ServerResponse> route2Example() {
        return RouterFunctions
                .route(RequestPredicates.GET("/otherExample/{id}"), exampleHandler::handleTwo)
                .andRoute(RequestPredicates.GET("/noRealReasonBehingThisEndpoint/{id}"), exampleHandler::handleTwo)
                .filter(new ExampleHandlerFilterFunction());
    }
}
