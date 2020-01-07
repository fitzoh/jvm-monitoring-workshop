package codemash;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@RestController
@SpringBootApplication
public class GaugeApplication {

    private static final Logger log = LoggerFactory.getLogger(GaugeApplication.class);
    private final MeterRegistry meterRegistry;

    //TODO use this when you refactor your instrumentation
    private final ActiveSessions activeSessions;

    public GaugeApplication(MeterRegistry meterRegistry, ActiveSessions activeSessions) {
        this.meterRegistry = meterRegistry;
        this.activeSessions = activeSessions;
    }

    public static void main(String[] args) {
        SpringApplication.run(GaugeApplication.class, args);
    }


    //TODO instrument this to track the total number of active sessions

    /**
     * @return a decaying Flux of Server Sent Events that sends a message after 10ms, then 20ms, then 30ms, etc
     */
    @GetMapping(value = {"/", "/ping"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<ServerSentEvent<String>> sse() {
        UUID sessionId = UUID.randomUUID();
        return Flux.range(0, Integer.MAX_VALUE)
                .delayUntil(i -> Mono.delay(Duration.ofMillis(i * 10)))
                .map(i -> ServerSentEvent.builder("ping " + sessionId).build())
                .doOnSubscribe(ignored -> {
                    log.info("starting SSE stream, session = {}", sessionId);
                })
                .doOnEach(signal -> {
                    log.debug("pinging {}", sessionId);
                })
                .doOnCancel(() -> {
                    log.info("cancelled SSE Stream, session = {}", sessionId);
                });
    }
}
