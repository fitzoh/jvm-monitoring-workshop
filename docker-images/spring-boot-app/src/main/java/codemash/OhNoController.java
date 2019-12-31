package codemash;

import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigInteger;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Use the endpoints here to manually trigger very bad things
 */
@RequestMapping("/oh-no")
@RestController
public class OhNoController {

    private static final String OH_NO = "oh no";
    private static final Logger log = LoggerFactory.getLogger(OhNoController.class);

    private final MeterRegistry meterRegistry;

    public OhNoController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Do bad things to the heap.
     * There's a gradual build-up, then *should* throw an exception and GC.
     */
    @GetMapping("/memory")
    public void memory(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("memory... {}!", OH_NO);
        LongTaskTimer.Sample sample = meterRegistry.more().longTaskTimer("oh.no", "type", "memory").start();

        /*
         * Repeat until the heap blows with a small delay between each step to stretch things out a bit
         * Set<String> accumulator = new HashSet<>()
         * accumulator.add("oh no")
         * accumulator.add("oh no!oh no")
         * accumulator.add("oh no!oh no!oh no")
         */
        Mono.just(OH_NO)
                .expand(s -> Mono.just(String.join("!", s, OH_NO)))
                .delayElements(Duration.ofMillis(2))
                .collect(HashSet::new, Set::add)
                .subscribeOn(Schedulers.elastic())
                .doFinally(signal -> sample.stop())
                .subscribe();
    }

    /**
     * Do bad things to the CPU (for about a minute)
     */
    @GetMapping("/cpu")
    public void cpu(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("cpu... {}!", OH_NO);
        LongTaskTimer.Sample sample = meterRegistry.more().longTaskTimer("oh.no", "type", "cpu").start();

        /*
         * Repeat for 1 minute on a bunch of threads to do bad things to the CPU
         * BigInteger accumulator = new BigInteger();
         * String value = String.valueOf("oh no".hashCode());
         * while (true){
         *   accumulator.add(new BigInteger(value);
         *   value = String.valueOf(value.hashCode());
         * }
         */
        Flux.just(String.valueOf(OH_NO.hashCode()))
                .expand(s -> Mono.just(String.valueOf(s.hashCode())))
                .map(BigInteger::new)
                .parallel(50)
                .runOn(Schedulers.elastic())
                .reduce(BigInteger::add)
                .timeout(Duration.ofMinutes(1))
                .subscribeOn(Schedulers.elastic())
                .doFinally(signal -> sample.stop())
                .subscribe();
    }

    @GetMapping("/dumpster-fire")
    public void dumpsterFire(ServerWebExchange exchange) {
        cpu(exchange);
        memory(exchange);
    }
}
