# MyBatis-Plus

## MyBatis-Plus介绍
[MyBatis-Plus官网](https://mybatis.plus/guide/)

MyBatis-Plus（简称 MP）是一个MyBatis 的增强工具，在MyBatis的基础上只做增强不做改变，为简化开发、提高效率而生。

### 特性
- 无侵入：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
- 损耗小：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
- 强大的 CRUD 操作：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- 支持 Lambda 形式调用：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
- 支持主键自动生成：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
- 支持 ActiveRecord 模式：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
- 支持自定义全局通用操作：支持全局通用方法注入（ Write once, use anywhere ）
- 内置代码生成器：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
- 内置分页插件：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
- 分页插件支持多种数据库：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库
- 内置性能分析插件：可输出 Sql 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
- 内置全局拦截插件：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

### 支持数据库
mysql 、 mariadb 、 oracle 、 db2 、 h2 、 hsql 、 sqlite 、 postgresql 、 sqlserver

### 框架结构

![image-20200928093018781](https://gitee.com/JeanLv/study_image/raw/master///image-20200928093018781.png)



## 快速入门

1. 创建SpringBoot工程

   1. 引入依赖

      ```xml
      <!--引入mybatis-plus的依赖-->
      <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.4.0</version>
      </dependency>
      
      <!--引入阿里巴巴druid-->
      <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.23</version>
      </dependency>
      
      <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.20</version>
      </dependency>
      ```

      - 注意：`不需要在引入mybatis的相关依赖,只引入这一个即可,当然数据库相关的驱动还的显式引入`

   2. 在SpringBoot入口类加入注解

      ```java
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
      ```

      

   3. 编写配置文件

      ```properties
      spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
      spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
      spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
      spring.datasource.username=root
      spring.datasource.password=123123
      logging.level.root=info
      logging.level.com.example.mybatis.plus.dao=debug
      ```

      

2. 创建数据库及表结构

   1. 表与mybatis的示例一致
   2. 开发实体类
   3. 开发mapper通用实现