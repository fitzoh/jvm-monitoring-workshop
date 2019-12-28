package codemash;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DimensionalCounterApplication {

    PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static void main(String[] args) {
        SpringApplication.run(DimensionalCounterApplication.class, args);
    }

    @GetMapping(value = {"/", "/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        //+=1
        Counter.builder("dimensional")
                //Notice the description
                .description("a counter that has multiple dimensions")
                .tag("increment-by", "one")
                .tag("conference", "codemash")
                .register(meterRegistry).increment(1);

        //+=2
        Counter.builder("dimensional")
                //Notice the lack of description (only one per name)
                .tag("increment-by", "two")
                .tag("conference", "some-other-conference")
                .register(meterRegistry).increment(2);

        //+=3
        Counter.builder("dimensional")
                .tag("increment-by", "three")
                .tag("conference", "also-the-wrong-conference")
                .register(meterRegistry).increment(3);

        return meterRegistry.scrape();
    }
}
