package codemash;

import codemash.utils.LatencyGenerator;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class TimerApplication {


    Logger log = LoggerFactory.getLogger(TimerApplication.class);
    PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    LatencyGenerator latencyGenerator = new LatencyGenerator();

    public static void main(String[] args) {
        SpringApplication.run(TimerApplication.class, args);
    }

    //TODO time this manually
    @GetMapping(value = {"/fast"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public void fast() throws Exception {
        latencyGenerator.fast().wait();
    }


    //TODO time this with a sample
    @GetMapping(value = {"/medium"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public void medium() throws Exception {
        latencyGenerator.medium().wait();
    }


    //TODO time this with a lambda
    @GetMapping(value = {"/slow"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public void slow() throws Exception {
        latencyGenerator.slow().wait();
    }

    //TODO time this with a long task timer
    @GetMapping(value = {"/really-slow"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public void reallySlow() throws Exception {
        latencyGenerator.reallySLow().wait();
    }

    @GetMapping(value = {"/scrape"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        return prometheusMeterRegistry.scrape();
    }
}
