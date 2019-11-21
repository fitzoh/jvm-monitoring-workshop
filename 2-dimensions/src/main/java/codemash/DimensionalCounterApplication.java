package codemash;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableScheduling
@RestController
@SpringBootApplication
public class DimensionalCounterApplication {

    private final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static void main(String[] args) {
        SpringApplication.run(DimensionalCounterApplication.class, args);
    }

    @GetMapping(value = "/scrape", produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        return registry.scrape();
    }


    @Scheduled(fixedRate = 1)
    public void incrementOneMilli() {
        registry.counter("dimensional", "period", "1ms").increment();
    }

    @Scheduled(fixedRate = 1000)
    public void incrementOneSec() {
        registry.counter("dimensional", "period", "1s").increment();
    }

    @Scheduled(fixedRate = 5000)
    public void incrementFiveSec() {
        registry.counter("dimensional", "period", "5s").increment();
    }
}
