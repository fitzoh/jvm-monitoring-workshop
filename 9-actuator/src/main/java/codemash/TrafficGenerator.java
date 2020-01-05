package codemash;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

/**
 * Sends a request to a random endpoint (with a random HTTP method) every 50ms
 */
@Component
public class TrafficGenerator implements ApplicationListener<ApplicationReadyEvent> {

    private final WebClient client;
    private final Random random = new Random();

    public TrafficGenerator(WebClient.Builder builder) {
        this.client = builder
                .baseUrl("http://localhost:8009")
                .build();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Flux.interval(Duration.ofMillis(50))
                .flatMap(i -> randomRequest().uri(urlTemplate(), i).exchange())
                .subscribe();
    }

    /**
     * pick a weighted random endpoint to hit (small chance of 404)
     */
    public String urlTemplate() {
        float roll = random.nextFloat();
        if (roll < .6) {
            return "/first/{id}";
        } else if (roll < .9) {
            return "/second/{id}";
        } else if (roll < .99) {
            return "/third/{id}";
        } else {
            return "/missing";
        }
    }


    /**
     * Pick the HTTP method to use
     */
    public WebClient.RequestHeadersUriSpec<?> randomRequest() {
        float roll = random.nextFloat();
        if (roll < .7) {
            return client.get();
        } else {
            return client.post();
        }
    }
}
