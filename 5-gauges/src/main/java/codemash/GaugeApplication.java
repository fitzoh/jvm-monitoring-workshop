package codemash;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@SpringBootApplication
public class GaugeApplication {


    Logger log = LoggerFactory.getLogger(GaugeApplication.class);
    PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);


    public static void main(String[] args) {
        SpringApplication.run(GaugeApplication.class, args);
    }


    //TODO instrument this to track the total number of active sessions
    @GetMapping(value = {"/"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<ServerSentEvent<String>> sse() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> ServerSentEvent.builder("ping").build())
                .doOnSubscribe(ignored -> log.info("starting SSE stream"))
                .doOnCancel(() -> log.info("cancelled SSE Stream"));
    }

    @GetMapping(value = {"/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        return prometheusMeterRegistry.scrape();
    }
}
