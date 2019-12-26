package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RegistryApplication {

    private final PrometheusMeterRegistry prometheus;
    private final Counter counter;

    public RegistryApplication(MeterRegistry composite, PrometheusMeterRegistry prometheus) {
        this.prometheus = prometheus;
        //TODO fix this metric name without editing this file
        this.counter = composite.counter("pormetheos.scrapes");
    }

    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class, args);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        counter.increment();
        return prometheus.scrape();
    }

}
