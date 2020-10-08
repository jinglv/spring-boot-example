package com.example.shiro;

import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jingLv
 * @date 2020/09/23
 */
@SpringBootApplication(exclude = {ShiroAutoConfiguration.class})
@MapperScan("com.example.shiro.dao")
public class SpringBootShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShiroApplication.class, args);
    }

}
