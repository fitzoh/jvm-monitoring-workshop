package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static codemash.CounterApplication.PROMETHEUS_SCRAPES;


/**
 * This class simulates a restart of the spring boot application by resetting the primary counters
 * (You can't actually reset a counter, so it removes them and then lets them get re-added)
 */
@RestController
public class RestartSimulator {


    private final MeterRegistry meterRegistry;
    private final Counter resets;

    public RestartSimulator(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        resets = Counter.builder("restarts")
                .description("the number of times that a restart has been simulated for the `" + PROMETHEUS_SCRAPES + "` metric")
                .register(meterRegistry);
    }

    @GetMapping("/simulate-restart")
    private void resetCounters() {
        meterRegistry.find(PROMETHEUS_SCRAPES)
                .counters()
                .forEach(meterRegistry::remove);
        resets.increment();
    }
}
