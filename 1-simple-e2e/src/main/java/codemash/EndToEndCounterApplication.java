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
public class EndToEndCounterApplication {

    PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    Counter counter = meterRegistry.counter("simple");

    public static void main(String[] args) {
        SpringApplication.run(EndToEndCounterApplication.class, args);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        counter.increment();
        return meterRegistry.scrape();
    }
}