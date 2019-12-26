package codemash;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CompositeRegistryTest {
    
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


}
