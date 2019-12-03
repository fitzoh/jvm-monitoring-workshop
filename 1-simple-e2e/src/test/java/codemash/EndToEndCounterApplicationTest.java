package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EndToEndCounterApplicationTest {

    @Test
    void id() {
        Counter counter = new SimpleMeterRegistry().counter("simple");

        assertThat(counter.getId().getName()).isEqualTo("simple");
    }

    @Test
    void reuseMeters() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter counter1 = registry.counter("simple");
        Counter counter2 = registry.counter("simple");

        assertThat(counter1).isSameAs(counter2);
    }


    @Test
    void prometheusIsScrapeable() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.counter("simple").increment();

        assertThat(registry.scrape()).contains("simple_total 1.0");
    }
}