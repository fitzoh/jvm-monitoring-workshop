package codemash;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DimensionalCounterApplication {

    PrometheusMeterRegistry meterRegistry;

    public DimensionalCounterApplication() {
        meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        meterRegistry.config().commonTags("conference", "codemash");
    }

    public static void main(String[] args) {
        SpringApplication.run(DimensionalCounterApplication.class, args);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        meterRegistry.counter("dimensional", "increment-by", "one").increment(1);
        meterRegistry.counter("dimensional", "increment-by", "two").increment(2);
        meterRegistry.counter("dimensional", "increment-by", "three").increment(3);
        return meterRegistry.scrape();
    }
}
