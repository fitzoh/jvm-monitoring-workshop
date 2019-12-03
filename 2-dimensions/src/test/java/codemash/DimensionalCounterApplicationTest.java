package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DimensionalCounterApplicationTest {

    @Test
    void sameNameAndTagsAreTheSame() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter counter1 = registry.counter("counter", "key", "value");
        Counter counter2 = registry.counter("counter", "key", "value");

        assertThat(counter1).isSameAs(counter2);
    }

    @Test
    void differentTagsMeansDifferentMeters() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter counter1 = registry.counter("counter", "key", "value1");
        Counter counter2 = registry.counter("counter", "key", "value2");

        assertThat(counter1).isNotEqualTo(counter2);
    }

    @Test
    void canHaveCommonTags() {
        MeterRegistry registry = new SimpleMeterRegistry();
        registry.config().commonTags("conference", "codemash");
        Counter counter = registry.counter("counter", "key", "value");

        assertThat(counter.getId().getTags()).hasSize(2);
    }

    @Test
    void orderingMatters() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter counter1 = registry.counter("counter", "key", "value");
        registry.config().commonTags("conference", "codemash");
        Counter counter2 = registry.counter("counter", "key", "value");


        assertThat(counter1.getId().getTags()).hasSize(1);
        assertThat(counter2.getId().getTags()).hasSize(2);
        assertThat(counter1).isNotEqualTo(counter2);
    }

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