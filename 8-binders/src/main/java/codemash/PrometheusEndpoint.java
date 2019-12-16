package codemash;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometheusEndpoint {

    private final PrometheusMeterRegistry registry;

    public PrometheusEndpoint(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    @GetMapping(value = {"/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        return registry.scrape();
    }
}
