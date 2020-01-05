package codemash;

import codemash.utils.LatencyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

import static org.springframework.web.reactive.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;


@RestController
@SpringBootApplication
public class ActuatorApplication {

    private final Random random = new Random();
    private final LatencyGenerator latencyGenerator = new LatencyGenerator();

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

    @RequestMapping(value = {"/first/{id}", "/second/{id}", "/third/{id}"})
    public Mono endpoint(ServerWebExchange exchange) {
        if (random.nextFloat() > .99) {
            throw new BadLuckException("you got a bad roll");
        }
        return Mono.just(exchange)
                .delayElement(getLatency(exchange))
                .map(ex -> ex.getResponse().setStatusCode(HttpStatus.OK));
    }


    /**
     * Determine how long the request should take
     */
    private Duration getLatency(ServerWebExchange exchange) {
        float roll = random.nextFloat();
        Duration baseLatency = latencyTax(exchange);
        if (roll < .5) {
            return latencyGenerator.fast().plus(baseLatency);
        } else if (roll < .8) {
            return latencyGenerator.slow().plus(baseLatency);
        } else {
            return latencyGenerator.reallySlow().plus(baseLatency);
        }
    }

    /**
     * Add a fixed amount of latency based on the endpoint requested (so that graphs are more interesting
     */
    private Duration latencyTax(ServerWebExchange exchange) {
        String template = exchange.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        if (template.startsWith("/first")) {
            return Duration.ofMillis(0);
        } else if (template.startsWith("/second")) {
            return Duration.ofMillis(250);
        } else {
            return Duration.ofSeconds(1);
        }
    }
}
