package codemash;

import codemash.utils.LatencyGenerator;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimerApplicationConfiguration {
    @Bean
    LatencyGenerator latencyGenerator() {
        return new LatencyGenerator();
    }

    @Bean
    PrometheusMeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }


}
