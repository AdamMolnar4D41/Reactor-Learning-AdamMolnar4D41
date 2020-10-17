package org.example.reactor.getValue;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Scheduling {

    private void schedulingMultipleFlux() {
        Flux.just("A", "B", "C")
                .publishOn(Schedulers.boundedElastic())
                .subscribe(letter -> System.out.println(Thread.currentThread().getName() + " " + letter));
        Flux.just("D", "E", "F")
                .publishOn(Schedulers.boundedElastic())
                .subscribe(letter -> System.out.println(Thread.currentThread().getName() + " " + letter));
    }

    private void scheduleSingleThread() {
        Flux.interval(Duration.ofSeconds(1), Schedulers.newSingle("singleThread"))
                .subscribe(num -> System.out.println(Thread.currentThread().getName() + " " + num));
    }

    private void scheduleAFlux() {
        Flux<String> flux = Flux.range(0, 10)
                .publishOn(Schedulers.newParallel("parallel-scheduler", 4))
                .delayElements(Duration.ofSeconds(1))
                .map(num -> Thread.currentThread().getName() + " " + num);
        flux.subscribe(System.out::println);
        /*  parallel-2 0
            parallel-3 1
            parallel-4 2
            parallel-5 3
            parallel-2 4
            parallel-3 5
            parallel-4 6
            parallel-5 7
            parallel-2 8
            parallel-3 9 */
    }
}
