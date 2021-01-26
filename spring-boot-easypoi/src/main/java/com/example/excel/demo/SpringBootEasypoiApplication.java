package com.example.excel.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jinglv
 * @date 2021/01/26
 */
@SpringBootApplication
@MapperScan("com.example.excel.demo.dao")
public class SpringBootEasypoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootEasypoiApplication.class, args);
    }

}
