package codemash;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;


@RestController
@SpringBootApplication
public class DistributionSummaryApplication {


    Logger log = LoggerFactory.getLogger(DistributionSummaryApplication.class);

    //TODO create a distribution summary and record the file upload sizes
    //TODO also track the ids, which should be a super boring distribution
    public DistributionSummaryApplication(MeterRegistry registry) {
    }

    public static void main(String[] args) {
        SpringApplication.run(DistributionSummaryApplication.class, args);
    }

    @PostMapping(value = {"/upload/{id}"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String upload(ServerWebExchange exchange, @PathVariable int id) throws Exception {
        long contentLength = exchange.getRequest().getHeaders().getContentLength();
        return String.format("id = %d, Content-Length = %d", id, contentLength);
    }


}
