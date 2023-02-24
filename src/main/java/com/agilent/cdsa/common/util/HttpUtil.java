package com.agilent.cdsa.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class HttpUtil {

    private RestTemplate restTemplate;
    private static HttpUtil httpUtil;

    @PostConstruct
    public void init() {
        httpUtil = this;
        httpUtil.restTemplate = new RestTemplate();
    }

    public static <T, K> K httpRequest(String url, HttpMethod method, HttpEntity<T> entity, ParameterizedTypeReference<K> responseType) {
        try {
            //发起一个POST请求
            ResponseEntity<K> result = httpUtil.restTemplate.exchange(url, method, entity, responseType);
            return result.getBody();
        } catch (Exception e) {
            log.error("请求失败： " + e.getMessage());
        }
        return null;
    }

}
