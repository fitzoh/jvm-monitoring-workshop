package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DimensionalCounterApplicationTest {

    /**
     * If the name, tags, and type (counter/gauge/timer/etc) are the same, the meters are the same
     * Duplicate counters incur a small penalty (hash map lookup),
     * so if you're in a tight loop store a reference if you can
     */
    @Test
    void sameNameAndTagsAreTheSame() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter counter1 = registry.counter("counter", "key", "value");
        Counter counter2 = registry.counter("counter", "key", "value");

        assertThat(counter1.getId()).isEqualTo(counter2.getId());
        //Note that this is reference equality, it pulled out the cached instance
        assertThat(counter1).isSameAs(counter2);
    }

    @Test
    void differentTagsMeansDifferentMeters() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter counter1 = registry.counter("counter", "key", "value1");
        Counter counter2 = registry.counter("counter", "key", "value2");

        assertThat(counter1).isNotEqualTo(counter2);
    }

    /**
     * Prometheus requires that any time series with the same name have the same set of label keys
     */
    @Test
    void prometheusRequiresMetersWithTheSameNameHaveTheSameLabelKeys() {
        PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        ThrowableAssert.ThrowingCallable notGonnaWork = () -> {
            prometheusMeterRegistry.counter("counter", "key", "value");
            prometheusMeterRegistry.counter("counter", "differentKey", "value2");
        };

        assertThatThrownBy(notGonnaWork)
                .hasMessage("Prometheus requires that all meters with the same name have the same set of tag keys. " +
                        "There is already an existing meter named 'counter_total' containing tag keys [key]. " +
                        "The meter you are attempting to register has keys [differentKey].");
    }

    /**
     * But some monitoring systems allow it, so micrometer allows it for non-prometheus registries
     */
    @Test
    void butMicrometerAllowsIt() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        registry.counter("counter", "key", "value");
        registry.counter("counter", "differentKey", "value");
    }

    /**
     * All Meters have a long form builder with more options
     * Also, the description becomes the prometheus HELP string
     */
    @Test
    void canAlsoAddTagsWithTheLongFormBuilder() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter.builder("counter")
                .tag("key", "value")
                .description("some kind of counter")
                .baseUnit("things")
                .register(registry);
    }
}