package codemash;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@SpringBootApplication
public class CounterApplication {

    public static final String PROMETHEUS_SCRAPES = "prometheus.scrapes";

    private final AtomicLong delta = new AtomicLong(1);
    private final PrometheusMeterRegistry registry;


    public CounterApplication(PrometheusMeterRegistry registry) {
        this.registry = registry;

        registry.more().counter("delta", Collections.emptyList(), delta);
    }

    public static void main(String[] args) {
        SpringApplication.run(CounterApplication.class, args);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        registry.counter("prometheus.scrapes", "delta", "one").increment();
        registry.counter("prometheus.scrapes", "delta", "two").increment(2);
        registry.counter("prometheus.scrapes", "delta", "dynamic").increment(delta.getAndIncrement());
        return registry.scrape();
    }
}
