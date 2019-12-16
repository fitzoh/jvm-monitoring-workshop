package codemash;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DistributionSummaryTest {

    SimpleMeterRegistry registry = new SimpleMeterRegistry();

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
    }

    @Test
    void theyExist() {
        DistributionSummary dist = DistributionSummary.builder("upload.size")
                .baseUnit("byte")
                .publishPercentileHistogram()
                .register(registry);

        dist.record(10);


        assertThat(dist.count()).isEqualTo(1);
        assertThat(dist.max()).isEqualTo(10);
        assertThat(dist.mean()).isEqualTo(10);
    }


    @Test
    void youCanScaleValues() {
        DistributionSummary dist = DistributionSummary.builder("upload.size")
                .baseUnit("byte")
                .scale(100)
                .publishPercentileHistogram()
                .register(registry);

        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            dist.record(random.nextFloat());
        }

        double mean = dist.mean();
        System.out.println("mean = " + mean);
        assertThat(mean).isCloseTo(50, Percentage.withPercentage(5));
    }

}
