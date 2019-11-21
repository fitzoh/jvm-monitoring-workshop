package com.github.fitzoh.codemash;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@EnableScheduling
//@RestController
//@SpringBootApplication
@Profile("nope")
public class SimpleCounterApplication {

    private final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static void main(String[] args) {
        SpringApplication.run(SimpleCounterApplication.class, args);
    }

    @GetMapping(value = "/scrape", produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        return registry.scrape();
    }

    @Scheduled(fixedRate = 1000)
    public void increment() {
        registry.counter("simple").increment();
    }
}
