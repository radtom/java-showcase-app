package cz.radtom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JavaShowcaseApplication {
     static void main(final String[] args) {
        SpringApplication.run(JavaShowcaseApplication.class, args);
    }
}
