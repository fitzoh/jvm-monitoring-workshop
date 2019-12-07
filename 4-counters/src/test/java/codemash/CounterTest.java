package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterTest {

    SimpleMeterRegistry registry = new SimpleMeterRegistry();

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
    }

    @Test
    void countersGoUp() {
        Counter counter = registry.counter("counter");

        counter.increment();

        assertThat(counter.count()).isEqualTo(1);
    }

    @Test
    void countersGoUpByMultiples() {
        Counter counter = registry.counter("counter");

        counter.increment(1000);

        assertThat(counter.count()).isEqualTo(1000);
    }

    @Test
    void canTrackIncreasingNumbers() {
        AtomicInteger state = new AtomicInteger();
        FunctionCounter fc = registry.more().counter("counter", Collections.emptyList(), state);

        state.addAndGet(100);

        assertThat(fc.count()).isEqualTo(100);
    }


    @Test
    void canTrackWithLambdas() {
        SomeThingThatIncreases increasingThing = new SomeThingThatIncreases();
        FunctionCounter fc = registry.more().counter("counter", Collections.emptyList(), increasingThing, SomeThingThatIncreases::getValue);

        increasingThing.increase();

        assertThat(fc.count()).isEqualTo(10);
    }

    static class SomeThingThatIncreases {
        int value = 0;

        void increase() {
            value += 10;
        }

        int getValue() {
            return value;
        }
    }
}
