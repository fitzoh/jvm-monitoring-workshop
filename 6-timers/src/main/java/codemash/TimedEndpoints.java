package codemash;

import codemash.utils.LatencyGenerator;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@RestController
public class TimedEndpoints {

    private final LatencyGenerator latencyGenerator;
    private final Random random = new Random();

    private final MeterRegistry meterRegistry;

    private List<Callable<Duration>> waitMethods = Arrays.asList(this::fast, this::medium, this::slow, this::reallySlow);

    public TimedEndpoints(LatencyGenerator latencyGenerator, MeterRegistry meterRegistry) {
        this.latencyGenerator = latencyGenerator;
        this.meterRegistry = meterRegistry;
    }

    private Timer timer(String bucket) {
        return Timer.builder("http.server.requests")
                .tag("bucket", bucket)
//                .sla(slaBuckets())
                .publishPercentileHistogram()
                .publishPercentiles(0.75, 0.9, 0.99)
                .register(meterRegistry);
    }

    private LongTaskTimer ltt() {
        return LongTaskTimer.builder("http.server.requests.really.slow")
                .register(meterRegistry);
    }

    @GetMapping(value = {"/work"})
    public Mono doWait() {
        return Mono.fromCallable(this.randomWait());
    }

    public Duration fast() throws Exception {
        long start = System.currentTimeMillis();

        Duration latency = latencyGenerator.fast();
        Thread.sleep(latency.toMillis());
        timer("fast").record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);

        return latency;
    }

    public Duration medium() throws Exception {
        Timer.Sample sample = Timer.start();

        Duration latency = latencyGenerator.medium();
        Thread.sleep(latency.toMillis());

        sample.stop(timer("medium"));
        return latency;
    }

    public Duration slow() throws Exception {

        return timer("slow").record(() -> {
            Duration latency = latencyGenerator.slow();
            try {
                Thread.sleep(latency.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return latency;
        });
    }

    public Duration reallySlow() throws Exception {
        Timer.Sample timerSample = Timer.start();
        LongTaskTimer.Sample lttSample = ltt().start();

        Duration latency = latencyGenerator.reallySlow();
        Thread.sleep(latency.toMillis());


        timerSample.stop(timer("really-slow"));
        lttSample.stop();
        return latency;
    }

    private Callable randomWait() {
        int idx = random.nextInt(waitMethods.size());
        return waitMethods.get(idx);
    }

    private Duration[] slaBuckets() {
        return Stream.of(10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000)
                .map(Duration::ofMillis)
                .toArray(Duration[]::new);
    }
}
