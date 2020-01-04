package codemash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class TrafficGenerator implements ApplicationListener<ApplicationReadyEvent> {

    private static Logger log = LoggerFactory.getLogger(TrafficGenerator.class);
    private final WebClient client;


    public TrafficGenerator(@Value("${server.port}") String port, WebClient.Builder builder) {
        client = builder.baseUrl("http://localhost:" + port)
                .build();

    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Flux.interval(Duration.ofMillis(500))
                .flatMap(ignored -> client.get().uri("/work").exchange())
                .subscribe(resp -> log.info(resp.statusCode().toString()));
    }

}
