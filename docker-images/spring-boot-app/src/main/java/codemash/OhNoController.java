package codemash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@RestController
public class OhNoController {

    String OH_NO = "oh no";
    Logger log = LoggerFactory.getLogger(OhNoController.class);

    @GetMapping("/oh-no")
    public void ohNo(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(OH_NO);
        Mono.just(OH_NO)
                .expand(s -> Mono.just(String.join("!", s, OH_NO)))
                .delayElements(Duration.ofMillis(1))
                .collect(HashSet::new, Set::add)
                .subscribeOn(Schedulers.elastic())
                .subscribe();

    }
}
