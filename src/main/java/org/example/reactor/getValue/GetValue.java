package org.example.reactor.getValue;

import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GetValue {

    private void simpleSubscribe() {
        // Does not return a value
        // But it starts the modification of the data
        // Can be useful if one of the mapping sets the data in an other object (see example)
        Flux<Integer> seq = Flux.range(0, 3);
        seq.subscribe();

        // Example:
        ValueHolder holder = new ValueHolder();
        Flux<String> exampleFlux = Flux.just("foo");
        exampleFlux.map(str -> {
            holder.setValue(str);
            return str;
        }).subscribe();
        System.out.println(holder.getValue());
    }

    private void subscribeWithAction() {
        // Does not return a value
        // It start the modification of the data
        // Does an action at the end of the flow (previous example to see how to request the data)
        Flux<Integer> seq = Flux.range(0, 3);
        seq.subscribe(System.out::println); // i -> System.out.println(i)
    }

    private void subscribeWithErrorHandling() {
        // Built in error handling if in case of an exception
        Flux<Integer> seq = Flux.range(0, 3)
                .map(i -> {
                    if (i <= 1) return i;
                    throw new RuntimeException("It was 2");
                });
        seq.subscribe(System.out::println,
                error -> System.err.println("Error: " + error));
    }

    private void subscribeWithActionWhenCompleted() {
        // Does something when it is done
        Flux<Integer> seq = Flux.range(0, 3);
        seq.subscribe(System.out::println,
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Done"));
    }

    private void subscribeWithLimitation() {
        // Request up to x elements
        // It will not write out the "Done" why?
        Flux<Integer> seq = Flux.range(0, 3);
        seq.subscribe(System.out::println,
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Done"),
                sub -> sub.request(1));
    }

    private void cancel() {
        Flux<Integer> seq = Flux.range(0, 3);
        Disposable disposable = seq.subscribe();
        // It is a signal that the source should stop producing elements. NOT an immediate stop.
        // If the source is too fast it can finish before receiving the stop signal.
        disposable.dispose();

        //Disposable swapping (Useful for example in UI scenarios)
        Disposable otherDisposable = Flux.range(6, 21).subscribe();
        Disposable.Swap swap = Disposables.swap();
        swap.replace(disposable);
        swap.replace(otherDisposable);
        swap.dispose(); // closes it

        // Batch disposing
        Disposable.Composite composite = Disposables.composite(disposable, otherDisposable);
        composite.dispose();
    }

    private void blocking() {
        Mono<String> str = Mono.just("foo");
        String blocked = str.block(); // Blocks the execution until the flow is done
        String blockUntil = str.block(Duration.ofSeconds(10)); // Blocks until the timeout expires
        Optional<String> blockOptional = str.blockOptional();// Same as block() but it returns an Optional
        Optional<String> blockUntilOptional = str.blockOptional(Duration.ofSeconds(10));

        Flux<String> flux = Flux.just("foo", "bar");
        String first = flux.blockFirst(); // Blocks until the first value arrives or the flow completes
        flux.blockFirst(Duration.ofSeconds(10)); // Blocks until the timeout expires
        String last = flux.blockLast(); // Blocks until the last value arrives or the flow completes
        flux.blockLast(Duration.ofSeconds(10));

        Iterable<String> iterable = flux.toIterable(); // Blocks on {@link Iterator#next()}
        Stream<String> stream = flux.toStream(); // Blocking for each source
        flux.toIterable(2);

        Mono<String> elementAt = flux.elementAt(0); // Returns the elements at index
        Mono<String> defaultElementAt = flux.elementAt(0, "default");
    }

    private class ValueHolder {
        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
