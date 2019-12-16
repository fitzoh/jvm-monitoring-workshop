package codemash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.function.Consumer;

@Component
@RestController
public class LogGenerator implements ApplicationRunner {

    Logger log = LoggerFactory.getLogger(LogGenerator.class);
    Random random = new Random();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Flux.interval(Duration.ofMillis(100))
                .map(i -> "message " + i)
                .subscribe(msg -> randomLog().accept(msg));

    }

    public Consumer<String> randomLog() {
        if (random.nextFloat() < .7) {
            return log::info;
        } else if (random.nextFloat() < .7) {
            return log::warn;
        } else {
            return log::error;
        }
    }
}
