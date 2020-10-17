package org.example.reactor.creation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ObjectCreation {

    private void createASingleObject(String str) {
        // 1. Empty object
        Mono<String> empty = Mono.empty();

        // 2. Object with data
        Mono<String> data = Mono.just("foo");
        // For rare scenarios
        Mono.<String>create(sink -> {
            if ("foo".equals(str)) {
                sink.success(str);
            } else {
                sink.error(new IllegalStateException("bar"));
            }
        });

        // 3. Object with error
        Mono<String> error = Mono.error(new IllegalStateException("bar"));
    }

    private void createASequenceOfObjects() {
        // 1. Empty "list"
        Flux<String> empty = Flux.empty();

        // 2. From multiple data
        Flux<String> seq1 = Flux.just("foo", "bar");

        // 3. From an array of data
        String[] array = {"foo", "bat"};
        Flux<String> seq2 = Flux.fromArray(array);

        // 4. From a list of data
        List<String> iterable = Arrays.asList("foo", "bar");
        Flux<String> seq3 = Flux.fromIterable(iterable);

        // 5. From a stream of data
        Flux<String> seq4 = Flux.fromStream(Stream.of("foo", "bar"));

        // 6. Range of items
        Flux<Integer> seq5 = Flux.range(0, 5);

        // 7. Generate
        Flux<String> seq6 = Flux.generate(
                () -> 0, // Initial index value
                (index, sink) -> {
                    sink.next("Index is " + index); // the operation to do
                    if (index == 5) sink.complete(); // terminate the flow (it will do the index 5 operation first
                    return ++index; // Add one to the index then restart it with the new index value
                }, (System.out::println)
        );
    }
}
