package com.agilent.cdsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.agilent.cdsa.*")
@SpringBootApplication
public class PrjApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrjApplication.class, args);
    }
}
