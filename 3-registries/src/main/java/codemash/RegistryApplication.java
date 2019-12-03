package codemash;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@SpringBootApplication
public class RegistryApplication {

    CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry();
    PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    LoggingMeterRegistry loggingMeterRegistry = new LoggingMeterRegistry(loggingConfig(), Clock.SYSTEM);

    Counter counter;

    public RegistryApplication() {
        compositeMeterRegistry.add(prometheusMeterRegistry);
        compositeMeterRegistry.add(loggingMeterRegistry);
        counter = Counter.builder("simple")
                .description("a simple counter")
                .baseUnit("thing")
                .tag("conference", "codemash")
                .register(compositeMeterRegistry);
    }

    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class, args);
    }


    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        counter.increment();
        return prometheusMeterRegistry.scrape();
    }

    private LoggingRegistryConfig loggingConfig() {
        return new LoggingRegistryConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(1);
            }
        };
    }
}
