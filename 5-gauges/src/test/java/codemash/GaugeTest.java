package codemash;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;

public class GaugeTest {

    SimpleMeterRegistry registry = new SimpleMeterRegistry();

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
    }

    @Test
    void youDontGetAGaugeReferenceByDefault() {
        AtomicInteger state = registry.gauge("inline", new AtomicInteger());

        assertThat(state).isNotInstanceOf(Gauge.class);
    }

    @Test
    void butYouCanWithTheLongFormBuilder() {
        AtomicInteger state = new AtomicInteger();
        Gauge gauge = Gauge.builder("long", state::get)
                .register(registry);

        state.incrementAndGet();

        assertThat(gauge.value()).isEqualTo(1);
    }

    @Test
    void youCanCreateAGaugeOffAnyNumber() {
        LongAdder state = registry.gauge("inline", new LongAdder());
    }

    @Test
    void youCanAlsoMeasureCollectionSizes() {
        List<String> state = registry.gaugeCollectionSize("inline", Arrays.asList(Tag.of("some", "tags")), new ArrayList<>());
    }

    @Test
    void orMapSizes() {
        Map<String, String> state = registry.gaugeMapSize("inline", Arrays.asList(Tag.of("some", "tags")), new HashMap<>());
    }

    @Test
    void youNeedToMaintainAReference() throws Exception {
        int expected = useWeakReference();

        System.gc();

        Gauge gauge = registry.find("weak.reference").gauge();
        assertThat(gauge.value()).isNotEqualTo(expected);
    }

    @Test
    void butYouCanMakeAStrongReferenceWithTheLongFormBUilder() throws Exception {
        int expected = useStrongReference();

        System.gc();

        Gauge gauge = registry.find("strong.reference").gauge();
        assertThat(gauge.value()).isEqualTo(expected);
    }

    private int useWeakReference() {
        return registry.gauge("weak.reference", new AtomicInteger()).incrementAndGet();
    }

    private int useStrongReference() {
        AtomicInteger state = new AtomicInteger();
        state.incrementAndGet();
        Gauge.builder("strong.reference", state::get)
                .strongReference(true)
                .register(registry);
        return state.incrementAndGet();
    }
}
