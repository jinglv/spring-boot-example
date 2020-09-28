package com.example.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MapperScan 加这种扫描这种方式更加灵活，只需要加一次
 *
 * @author lvjing
 * @date 2020/09/28
 */
@SpringBootApplication
@MapperScan("com.example.mybatis.plus.dao")
public class SpringBootMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisPlusApplication.class, args);
    }

}
