package com.lesso.neverland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NeverlandApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeverlandApplication.class, args);
    }

}
