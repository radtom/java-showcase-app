package cz.radtom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class JavaShowcaseApplication {
     static void main(final String[] args) {
        SpringApplication.run(JavaShowcaseApplication.class, args);
    }
}
