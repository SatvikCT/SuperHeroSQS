package org.example.superheroes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SuperheroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperheroApplication.class, args);
    }

}
