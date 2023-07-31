package com.agilent.iad.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${restTemplate.connectionRequestTimeout}")
    private int connectionRequestTimeout; //连接请求超时时间

    @Value("${restTemplate.connectionTimeout}")
    private int connectionTimeout;        //连接超时时间

    @Value("${restTemplate.readTimeout}")
    private int readTimeout;              //读取超时时间

    @Bean
    @Primary
    public RestTemplate customRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }

}