package com.example.mybatis.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jinglv
 * @date 2021/01/14
 */
@SpringBootApplication
@MapperScan("com.example.mybatis.annotation.mapper")
public class SpringBootMybatisAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisAnnotationApplication.class, args);
    }

}
