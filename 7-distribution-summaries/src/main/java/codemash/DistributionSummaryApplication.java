package codemash;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.stream.IntStream;


@RestController
@SpringBootApplication
public class DistributionSummaryApplication {

    DistributionSummary summary;

    public DistributionSummaryApplication(MeterRegistry registry) {
        summary = DistributionSummary.builder("upload.size")
                .sla(IntStream.range(0, 20).mapToDouble(i -> Math.pow(10, i)).mapToLong(i -> (long) i).toArray())
                .register(registry);
    }

    public static void main(String[] args) {
        SpringApplication.run(DistributionSummaryApplication.class, args);
    }

    @PostMapping(value = {"/upload"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String upload(ServerWebExchange exchange) throws Exception {
        long contentLength = exchange.getRequest().getHeaders().getContentLength();
        summary.record(contentLength);
        return String.format("Content-Length = %d", contentLength);
    }

}
