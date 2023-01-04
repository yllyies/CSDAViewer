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

/*@ComponentScan("com.agilent.cdsa.*")
@SpringBootApplication
public class PrjApplication_package extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(PrjApplication_package.class, args);
    }

    // 继承 SpringBootServletInitializer，起到web.xml作用
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PrjApplication_package.class);
    }
}*/
