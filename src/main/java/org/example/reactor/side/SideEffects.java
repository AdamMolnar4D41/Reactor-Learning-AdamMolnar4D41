package org.example.reactor.side;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.PooledDataBuffer;
import reactor.core.publisher.Mono;

public class SideEffects {

    private void sideEffects() {
        Mono<String> str = Mono.just("foo");
        str.doOnNext(System.out::println); // Triggered when the source emits a data successfully
        str.doOnEach(signal -> System.out.println(signal.get())); // Triggered when the source emits a data, an error, or terminates successfully
        str.doOnError(System.err::println); // Triggered when the source completes with an error
        str.doOnCancel(() -> System.out.println("Cancelled")); // Triggered when the source is cancelled
        str.doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release); // Triggered on discard
        str.doOnTerminate(() -> System.out.println("Terminated")); // Triggered when the source terminates either with data, empty or error
        str.doOnRequest(System.out::println); // Triggered when the source is requested
        str.doOnSubscribe(subscription -> System.out.println("Sub")); // Triggered when a consumer is subscribed to the source
        str.doOnSuccess(System.out::println); // Triggered when the source completes successfully
    }

    private void otherUsefulMethods() {
        Mono<String> mono = Mono.just("foo");
        mono.log(); //This will observe all reactive signal and log it with the provided logger and level

        // Altering the data
        Mono<String> mono1 = mono.map(str -> str + str); // foo -> foofoo
        Mono<String> mono2 = mono.flatMap(str -> Mono.just(str).map(s -> s + s)); // foo -> foofoo
        // Why we need flatmap?
        Mono<Mono<String>> monoMono = mono.map(srt -> Mono.just(srt).map(s -> s + s));

        mono.filter(s -> s.equals("filterMe")); // If the value matches the Predicate than returns it

        mono.flux(); // Converts the Mono to Flux

        mono.repeat(2); // re-subscribes to the producer 1 + numRepeat times,
        // numRepeat == 0 means that only one subscription will happen

        mono.switchIfEmpty(Mono.just("bar")); // Switch to the other Mono if the first one was empty
        mono.then(Mono.just("bar")); // After the first one completes starts the next one

        // Waits for the result of the first Mono then use the result to create a second Mono
        // and combine those by the provided function
        mono.zipWhen(str -> Mono.just("bar"), (foo, bar) -> foo + bar);
        // Combines the two Mono into one with the provided function
        // It can result in a very interesting bug in RxJava but Reactor solves it
        mono.zipWith(Mono.just("bar"), (foo, bar) -> foo + bar);
    }
}
