package codemash;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;


@RestController
@SpringBootApplication
public class DistributionSummaryApplication {

    DistributionSummary summary;

    public DistributionSummaryApplication(MeterRegistry registry) {
        //TODO record the file upload sizes and publish SLA buckets for each power of 2
        summary = DistributionSummary.builder("upload.size")
                .register(registry);
    }

    public static void main(String[] args) {
        SpringApplication.run(DistributionSummaryApplication.class, args);
    }

    @PostMapping(value = {"/upload"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String upload(ServerWebExchange exchange) throws Exception {
        long contentLength = exchange.getRequest().getHeaders().getContentLength();
        return String.format("Content-Length = %d", contentLength);
    }

}
