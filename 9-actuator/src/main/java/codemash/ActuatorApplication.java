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
            throw new BadLuckException("you got a bad roll[");
        }
        return Mono.just(exchange)
                .delayElement(getLatency())
                .map(ex -> ex.getResponse().setStatusCode(HttpStatus.OK));
    }


    private Duration getLatency() {
        float roll = random.nextFloat();
        if (roll < .5) {
            return latencyGenerator.fast();
        } else {
            return latencyGenerator.reallySLow();
        }
    }
}
