package com.agilent.iad.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createTestApi() {// 创建API基本信息
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.agilent.iad.controller"))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
//                .paths(PathSelectors.any())
                .build()
                .apiInfo(// 创建API的基本信息，这些信息会在Swagger UI中进行显示
                        new ApiInfoBuilder().title("Instrument Agile Panel")
                                .description("用于仪器实时状态，运行数据统计和分析")
                                .version("OpenAPI: 1.0.0")
                                .contact(new Contact("Agilent", "https://www.agilent.com.cn/", "michael.li2@agilent.com"))
                                .license("Apache License, Version 2.0")
                                .licenseUrl("https://apache.org/licenses/LICENSE-2.0")
                                .build());
    }
}
