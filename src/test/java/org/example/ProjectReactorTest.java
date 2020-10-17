package org.example;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

import java.time.Duration;

public class ProjectReactorTest {

    @Test
    public void sampleTest() {
        StepVerifier.create(Mono.just("foo"))
                .expectSubscription()
                .expectNext("foo")
                .expectComplete()
                .verify();

        StepVerifier.create(Mono.just("foo"))
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test(expected = AssertionError.class)
    public void doNotUseBothNextAndNextCount() {
        StepVerifier.create(Mono.just("foo"))
                .expectSubscription()
                .expectNext("foo")
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test(expected = AssertionError.class)
    public void failsAfterGivenTime() {
        StepVerifier.create(Mono.just("foo")
                .delayElement(Duration.ofSeconds(5)))
                .expectNextCount(1)
                .expectComplete()
                .verify(Duration.ofSeconds(2));
    }

    @Test
    public void manipulateTime() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofDays(1)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofDays(1))
                .expectNext(0L)
                .expectNoEvent(Duration.ofDays(1))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    public void readable() {
        StepVerifierOptions customizedScenario = StepVerifierOptions.create()
                .scenarioName("By using StepVerifierOptions we can provide name to the scenario");
        StepVerifier.create(Mono.just("foo"), customizedScenario)
                .expectNext("bar")
                .as("The contained string should be 'bar'")
                .verifyComplete();
    }
}