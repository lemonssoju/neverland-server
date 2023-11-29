package com.lesso.neverland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class NeverlandApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeverlandApplication.class, args);
    }

}
