package codemash;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@SpringBootApplication
public class CounterApplication {

    public static final String PROMETHEUS_SCRAPES = "prometheus.scrapes";

    //TODO add a counter to track this value
    private final AtomicLong delta = new AtomicLong(1);
    private final PrometheusMeterRegistry registry;


    public CounterApplication(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    public static void main(String[] args) {
        SpringApplication.run(CounterApplication.class, args);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        //TODO add meaningful tags to these counters
        registry.counter("prometheus.scrapes", "delta", "TODO").increment();
        registry.counter("prometheus.scrapes", "delta", "TODO").increment(2);
        registry.counter("prometheus.scrapes", "delta", "TODO").increment(delta.getAndIncrement());
        return registry.scrape();
    }
}
