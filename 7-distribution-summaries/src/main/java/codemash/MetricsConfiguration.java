package codemash;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {
    @Bean
    PrometheusMeterRegistry registry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }
}
