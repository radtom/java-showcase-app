package cz.radtom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class JavaShowcaseApplication {
     static void main(final String[] args) {
        SpringApplication.run(JavaShowcaseApplication.class, args);
    }
}
