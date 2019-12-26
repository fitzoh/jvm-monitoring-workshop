package codemash;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.List;

@Configuration
public class MetricsConfiguration {

    @Bean
    PrometheusMeterRegistry prometheusMeterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    LoggingMeterRegistry loggingMeterRegistry() {
        return new LoggingMeterRegistry(loggingConfig(), Clock.SYSTEM);
    }

    @Bean
    @Primary
    MeterRegistry meterRegistry(List<MeterRegistry> registries) {
        CompositeMeterRegistry composite = new CompositeMeterRegistry();
        registries.forEach(composite::add);
        return composite;
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
