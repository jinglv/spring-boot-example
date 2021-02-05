package com.example.rest.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author jingLv
 * @date 2021/01/29
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 默认使用底层引擎，HTTPURLConnection
        return new RestTemplate();
    }
}
