package codemash.Utils;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class LatencyGenerator {

    private static final Duration maxFast = Duration.ofMillis(20);
    private static final Duration maxMedium = Duration.ofMillis(100);
    private static final Duration maxSlow = Duration.ofMillis(500);
    private static final Duration maxReallySlow = Duration.ofSeconds(10);

    private final Duration baseLatency;

    public LatencyGenerator() {
        baseLatency = Duration.ofMillis(10);
    }

    public Duration fast() {
        return randomRange(Duration.ZERO, maxFast);
    }

    public Duration medium() {
        return randomRange(maxFast, maxMedium);
    }

    public Duration slow() {
        return randomRange(maxMedium, maxSlow);
    }

    public Duration reallySLow() {
        return randomRange(maxSlow, maxReallySlow);
    }

    private Duration randomRange(Duration lower, Duration upper) {
        long millis = ThreadLocalRandom.current().nextLong(lower.toMillis(), upper.toMillis());
        return Duration.ofMillis(millis).plus(baseLatency);
    }
}
