package codemash;

import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TimerTest {

    SimpleMeterRegistry registry = new SimpleMeterRegistry();

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
    }

    @Test
    void youCanTimeStaticValues() {
        Timer timer = registry.timer("some.timer");

        timer.record(Duration.ofSeconds(1));
        timer.record(1, TimeUnit.SECONDS);

        assertThat(timer.totalTime(TimeUnit.SECONDS)).isEqualTo(2);
    }

    @Test
    void youCanTimeFunctions() {
        Timer timer = registry.timer("some.timer");


        Runnable r = () -> System.out.println("doing some stuff");
        timer.record(r);

        Supplier<String> s = () -> "here is a string";
        timer.record(s);

        Callable c = () -> {
            throw new RuntimeException("oops");
        };
        assertThatThrownBy(() -> timer.recordCallable(c)).hasMessage("oops");


        assertThat(timer.count()).isEqualTo(3);
    }

    @Test
    void youCanUseSamples() throws Exception {
        Timer.Sample sample = Timer.start(registry);

        Thread.sleep(1000);

        Timer timer = registry.timer("some.timer");
        sample.stop(timer);
        assertThat(timer.count()).isEqualTo(1);
    }

    @Test
    void whichLetsYouSetTagsDynamically() throws Exception {
        Timer.Sample sample = Timer.start(registry);

        Thread.sleep(1000);
        String success = String.valueOf(new Random().nextBoolean());

        Timer timer = registry.timer("some.timer", "success", success);
        sample.stop(timer);
    }

    @Test
    void ofCourseThereIsALongFormBuilder() {
        Timer.builder("some.timer")
                .description("it times things")
                .tag("some", "tags")
                .maximumExpectedValue(Duration.ofSeconds(0))
                .maximumExpectedValue(Duration.ofSeconds(10))
                .publishPercentileHistogram()
                .sla(Duration.ofMillis(100), Duration.ofSeconds(1))
                .register(registry);
    }

    @Test
    void thereAreAlsoLongTaskTimers() {
        LongTaskTimer ltt = registry.more().longTaskTimer("long.timer");

        LongTaskTimer.Sample sample = ltt.start();
        assertThat(ltt.activeTasks()).isEqualTo(1);
        sample.stop();

        assertThat(ltt.activeTasks()).isEqualTo(0);
    }
}
