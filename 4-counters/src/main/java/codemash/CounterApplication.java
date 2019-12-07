package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class CounterApplication {

    PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    Counter counter;

    public CounterApplication() {
        counter = Counter.builder("composite")
                .description("a simple counter")
                .baseUnit("thing")
                .tag("conference", "codemash")
                .register(prometheusMeterRegistry);
    }

    public static void main(String[] args) {
        SpringApplication.run(CounterApplication.class, args);
    }


    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        counter.increment();
        return prometheusMeterRegistry.scrape();
    }
}
