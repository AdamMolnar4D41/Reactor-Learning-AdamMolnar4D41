package org.example.reactor.error;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class ErrorHandling {

    private void fallbackValue() {
        Flux.range(0, 5)
                .map(i -> 10 / i) // Error if i = 0
                .onErrorReturn(10); //This is a fallback value
    }

    private void fallbackMethod() {
        Flux.range(0, 5)
                .map(i -> 10 / i)
                .onErrorResume(throwable -> Flux.just(1));
    }
}
