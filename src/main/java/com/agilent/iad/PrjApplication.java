package com.agilent.iad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.agilent.iad.*")
@SpringBootApplication
public class PrjApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrjApplication.class, args);
    }
}
