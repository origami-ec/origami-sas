package org.origami.docs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OrigamiDocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrigamiDocsApplication.class, args);
    }
   /* @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
    }*/
}
