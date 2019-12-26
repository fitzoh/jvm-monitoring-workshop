package codemash;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistryTest {

    @Test
    void compositeRegistriesHaveMultipleRegistries() {
        CompositeMeterRegistry composite = new CompositeMeterRegistry();

        for (int i = 0; i < 5; i++) {
            composite.add(new SimpleMeterRegistry());
        }

        assertThat(composite.getRegistries()).hasSize(5);
    }

    @Test
    void compositeRegistryMetersCascade() {
        CompositeMeterRegistry composite = new CompositeMeterRegistry();
        for (int i = 0; i < 5; i++) {
            composite.add(new SimpleMeterRegistry());
        }

        composite.counter("simple").increment();

        composite.getRegistries()
                .forEach(registry -> {
                    assertThat(registry.getMeters()).hasSize(1);
                    assertThat(registry.get("simple").counter().count()).isEqualTo(1);
                });
    }

    @Test
    void compositeRegistriesNoopUntilAChildRegistryIsPresent() {
        CompositeMeterRegistry composite = new CompositeMeterRegistry();

        Counter counter = composite.counter("nope");
        counter.increment();
        assertThat(counter.count()).isEqualTo(0);

        composite.add(new SimpleMeterRegistry());
        counter = composite.counter("yup");
        counter.increment();
        assertThat(counter.count()).isEqualTo(1);
    }

    @Test
    void thereIsAGlobalCompositeRegistry() {
        assertThat(Metrics.globalRegistry).isInstanceOf(CompositeMeterRegistry.class);
        assertThat(Metrics.counter("global")).isInstanceOf(Counter.class);
    }

    @Test
    void canSetConfigProperties() {
        Map<String, String> externalConfig = new HashMap<>();
        SimpleConfig config = new SimpleConfig() {

            @Override
            public String get(String key) {
                return externalConfig.get(key);
            }
        };
        SimpleMeterRegistry registry = new SimpleMeterRegistry(config, Clock.SYSTEM);
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
    void canModifyMeterIds() {
        MeterRegistry registry = new SimpleMeterRegistry();
        registry.config().meterFilter(new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                return id.withName("new").withTag(Tag.of("extra", "tag"));
            }
        });

        registry.counter("old");

        assertThat(registry.getMeters()).hasSize(1);
        assertThat(registry.find("old").counters()).hasSize(0);
        assertThat(registry.get("new").counters()).hasSize(1);
        assertThat(registry.get("new").counter().getId().getTag("extra")).isEqualTo("tag");

    }


    @Test
    void canRemoveMeters() {
        MeterRegistry registry = new SimpleMeterRegistry();
        MeterFilter nope = MeterFilter.denyNameStartsWith("nope");
        MeterFilter alsoNo = MeterFilter.deny(id -> id.getTag("ignore") != null);
        registry.config().meterFilter(nope).meterFilter(alsoNo);

        registry.counter("nope.counter");
        registry.counter("ok.but", "ignore", "me");
        registry.counter("this.one.works");
        assertThat(registry.getMeters()).hasSize(1);
    }

    @Test
    void canIgnoreTags() {
        MeterRegistry registry = new SimpleMeterRegistry();

        registry.config().meterFilter(MeterFilter.ignoreTags("ignoreme"));
        Counter counter = registry.counter("counter", "ignoreme", "ignored");

        assertThat(counter.getId().getTags()).hasSize(0);
    }

    @Test
    void canRenameTags() {
        MeterRegistry registry = new SimpleMeterRegistry();

        MeterFilter exciteFilter = MeterFilter.renameTag("my", "boring", "awesome");
        registry.config().meterFilter(exciteFilter);
        Counter counter = registry.counter("my.counter", "boring", "tag");

        assertThat(counter.getId().getTags().get(0)).isEqualTo(Tag.of("awesome", "tag"));
    }

    @Test
    void canModifyTagValues() {
        MeterRegistry registry = new SimpleMeterRegistry();

        MeterFilter exciteFilter = MeterFilter.replaceTagValues("excite", s -> s + "!");
        registry.config().meterFilter(exciteFilter);
        Counter counter = registry.counter("counter", "excite", "yay");

        assertThat(counter.getId().getTags().get(0).getValue()).isEqualTo("yay!");
    }
}
