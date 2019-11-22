package codemash;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@EnableScheduling
@RestController
@SpringBootApplication
public class RegistryApplication implements CommandLineRunner {

    //TODO tell people about Metrics.globalRegistry
    private final CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry();
    private final PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    private final LoggingMeterRegistry loggingMeterRegistry = new LoggingMeterRegistry(loggingConfig(), Clock.SYSTEM);

    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        compositeMeterRegistry.add(loggingMeterRegistry);
        compositeMeterRegistry.add(prometheusMeterRegistry);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        return prometheusMeterRegistry.scrape();
    }

    @Scheduled(fixedRate = 1000)
    public void increment() {
        compositeMeterRegistry.counter("simple").increment();
        Metrics.globalRegistry.counter("simple").increment();
    }

    private LoggingRegistryConfig loggingConfig() {
        return new LoggingRegistryConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(5);
            }
        };
    }
}
