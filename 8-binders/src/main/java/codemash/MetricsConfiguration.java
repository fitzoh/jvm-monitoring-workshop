package codemash;

import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class MetricsConfiguration {

    @Bean
    PrometheusMeterRegistry registry() {
        PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        //system
        new FileDescriptorMetrics().bindTo(meterRegistry);
        new UptimeMetrics().bindTo(meterRegistry);
        new ProcessorMetrics().bindTo(meterRegistry);

        //jvm
        new ClassLoaderMetrics().bindTo(meterRegistry);
        new DiskSpaceMetrics(new File(".")).bindTo(meterRegistry);
        new ExecutorServiceMetrics(null, "sample", null).bindTo(meterRegistry);
        new JvmGcMetrics().bindTo(meterRegistry);
        new JvmMemoryMetrics().bindTo(meterRegistry);
        new JvmThreadMetrics().bindTo(meterRegistry);

        //the best one
        new LogbackMetrics().bindTo(meterRegistry);

        return meterRegistry;
    }
}
