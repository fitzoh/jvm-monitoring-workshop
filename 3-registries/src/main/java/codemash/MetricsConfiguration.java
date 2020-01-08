package codemash;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
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
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        prometheusMeterRegistry.config().commonTags("registry", "prometheus");
        return prometheusMeterRegistry;
    }

    @Bean
    LoggingMeterRegistry loggingMeterRegistry() {
        LoggingMeterRegistry loggingMeterRegistry = new LoggingMeterRegistry(loggingConfig(), Clock.SYSTEM);
        loggingMeterRegistry.config().commonTags("registry", "log");
        return loggingMeterRegistry;
    }

    @Bean
    @Primary
    MeterRegistry meterRegistry(List<MeterRegistry> registries) {
        CompositeMeterRegistry composite = new CompositeMeterRegistry();
        composite.config().commonTags("codemash", "codemash");

        composite.config().meterFilter(new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                return id.withName("prometheus.scrapes");
            }
        });
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
