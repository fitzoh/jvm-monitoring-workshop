package codemash;

import org.springframework.boot.CommandLineRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

//@Component
public class TrafficGenerator implements CommandLineRunner {

    private final WebClient client;
    private final Random random = new Random();

    public TrafficGenerator(WebClient.Builder builder) {
        this.client = builder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Override
    public void run(String... args) throws Exception {
        Flux.interval(Duration.ofMillis(50))
                .flatMap(i -> randomRequest().uri(urlTemplate(), i).exchange())
                .subscribe();

    }

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


    public WebClient.RequestHeadersUriSpec<?> randomRequest() {
        float roll = random.nextFloat();
        if (roll < .5) {
            return client.get();
        } else if (roll < .8) {
            return client.post();
        } else if (roll < .9) {
            return client.put();
        } else {
            return client.delete();
        }
    }
}
