package codemash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class BindersApplication {

    public static void main(String[] args) {
        SpringApplication.run(BindersApplication.class, args);
    }

}
