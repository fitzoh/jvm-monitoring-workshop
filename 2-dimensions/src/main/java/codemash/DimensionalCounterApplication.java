package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@SpringBootApplication
public class DimensionalCounterApplication {

    PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static void main(String[] args) {
        SpringApplication.run(DimensionalCounterApplication.class, args);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        primary();
        if (ThreadLocalRandom.current().nextFloat() < .3) {
            secondary();
        }
        return meterRegistry.scrape();
    }

    private void primary() {
        Counter.builder("dimensional")
                //Notice we only need to define the description once
                .description("a counter that has multiple dimensions")
                .tag("conference", "codemash")
                .tag("method", "primary")
                .tag("delta", "one")
                .register(meterRegistry).increment(1);

        Counter.builder("dimensional")
                .tag("conference", "codemash")
                .tag("method", "primary")
                .tag("delta", "two")
                .register(meterRegistry).increment(2);

        Counter.builder("dimensional")
                .tag("conference", "codemash")
                .tag("method", "primary")
                .tag("delta", "three")
                .register(meterRegistry).increment(3);

    }

    private void secondary() {
        Counter.builder("dimensional")
                .tag("conference", "codemash")
                .tag("method", "secondary")
                .tag("increment-by", "one")
                .register(meterRegistry).increment(1);

        Counter.builder("dimensional")
                .description("a counter that has multiple dimensions")
                .tag("conference", "codemash")
                .tag("method", "secondary")
                .tag("increment-by", "two")
                .register(meterRegistry).increment(2);

        Counter.builder("dimensional")
                .description("a counter that has multiple dimensions")
                .tag("conference", "codemash")
                .tag("method", "secondary")
                .tag("increment-by", "three")
                .register(meterRegistry).increment(3);
    }
}
