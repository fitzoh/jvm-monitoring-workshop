package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DimensionalCounterApplicationTest {

    /**
     * If the name, tags, and type (counter vs gauge vs timer etc) are the same, the meters are the same
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