package codemash;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Component
public class TrafficGenerator implements ApplicationListener<ApplicationReadyEvent> {

    private final Random random = new Random();
    private final WebClient client;
    private final DistributionSummary summary;

    public TrafficGenerator(@Value("${server.port}") String port, WebClient.Builder builder, MeterRegistry registry) {
        client = builder.baseUrl("http://localhost:" + port)
                .build();
        //TODO configure the distribution min/max values and publish percentile histogram,
        //TODO then instrument the code to record the exponent
        summary = DistributionSummary.builder("exponent")
                .register(registry);

    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Flux.interval(Duration.ofMillis(50))
                .flatMap(ignored -> this.postRandomPayload())
                .subscribe();
    }


    private Mono<ClientResponse> postRandomPayload() {
        return client.post().uri("/upload").bodyValue(randomPayload()).exchange();
    }


    private byte[] randomPayload() {
        int exponent = 0;
        while ((random.nextBoolean() || random.nextBoolean()) && exponent++ < 20) {
            exponent++;
        }
        int size = random.nextInt((int) Math.pow(2, exponent));
        return new byte[size];
    }
}
