package com.agilent.iad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.agilent.iad.*")
@SpringBootApplication
public class PrjApplication extends SpringBootServletInitializer {
    // war 主程序入口
    public static void main(String[] args) {
        SpringApplication.run(PrjApplication.class, args);
    }

    // 继承 SpringBootServletInitializer，起到web.xml作用
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PrjApplication.class);
    }
}
