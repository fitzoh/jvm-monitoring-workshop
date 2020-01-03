package codemash;

import codemash.utils.LatencyGenerator;
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
                .register(meterRegistry);
    }

    @GetMapping(value = {"/wait"})
    public Mono doWait() {
        return Mono.fromCallable(this.randomWait());
    }

    //TODO time this manually
    public Duration fast() throws Exception {
        Duration latency = latencyGenerator.fast();
        Thread.sleep(latency.toMillis());
        return latency;
    }

    //TODO time this with a sample
    public Duration medium() throws Exception {
        Duration latency = latencyGenerator.medium();
        Thread.sleep(latency.toMillis());
        return latency;
    }

    //TODO time this with a lambda
    public Duration slow() throws Exception {
        Duration latency = latencyGenerator.slow();
        Thread.sleep(latency.toMillis());
        return latency;
    }

    //TODO time this with a long task timer
    public Duration reallySlow() throws Exception {
        Duration latency = latencyGenerator.reallySlow();
        Thread.sleep(latency.toMillis());
        return latency;
    }

    private Callable randomWait() {
        int idx = random.nextInt(waitMethods.size());
        return waitMethods.get(idx);
    }


}
