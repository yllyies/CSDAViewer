package com.agilent.csda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;


@ComponentScan("com.agilent.csda.*")
@SpringBootApplication
public class PrjApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrjApplication.class, args);
    }
}
