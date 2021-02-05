# SpringBoot集成RestTemplate

## RestTemplate简介
常见的Http客户端请求工具：
- JDK HttpURLConnection
- Apache HttpClient
- OkHttp

RestTemplate是一个同步的Web Http客户端请求模板工具，Spring框架做的抽象

[官方文档地址](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#rest-client-access)

RestTemplate默认使用HttpURLConnection，可以通过构造方法替换底层的执行引擎，常见的执行引擎包括Apache HttpClient、Netty、OkHttp，替换的操作如下：
```java
// 默认是使用的引擎是HttpURLConnection
RestTemplate template = new RestTemplate();
// 底层引擎切换，需要实现ClientHttpRequestFactory
RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
```
注意：RestTemplate在Spring5.0以后，就只处于维护模式，后期会推荐使用WebClient

## SpringBoot集成RestTemplate
SpringBoot项目中可以在Bean的配置类中直接声明RestTemplate
```java
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
```

## RestTemplate值Http Get请求
Http Get请求介绍：

RestTemplate有两个核心方法来执行Get请求：
1. RestTemplate.getForObject方法可以获取对象
2. RestTemplate.getForEntity方法不仅可以获取对象，还可以获取Http状态码，请求头等详细信息