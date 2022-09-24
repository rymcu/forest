package com.rymcu.forest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author ronger
 */
@EnableAsync
@SpringBootApplication
public class ForestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForestApplication.class, args);
    }

}
