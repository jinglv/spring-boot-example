package com.example.mybatis.druid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jinglv
 * @date 2021/01/26
 */
@SpringBootApplication
@MapperScan("com.example.mybatis.druid.mapper")
public class SpringBootMybatisDruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisDruidApplication.class, args);
    }

}
