# Spring Boot学习

[SpringBoot官方网站](https://spring.io/projects/spring-boot/)

Spring Boot 主要提供了如下功能：

1. 为所有基于 Spring 的 Java 开发提供方便快捷的入门体验。
2. 开箱即用，有自己自定义的配置就是用自己的，没有就使用官方提供的默认的。
3. 提供了一系列通用的非功能性的功能，例如嵌入式服务器、安全管理、健康检测等。
4. 绝对没有代码生成，也不需要XML配置。

Spring Boot 的出现让 Java 开发又回归简单，因为确确实实解决了开发中的痛点，因此这个技术得到了非常广泛的使用，松哥很多朋友出去面试 Java 工程师，从2017年年初开始，Spring Boot基本就是必问，现在流行的 Spring Cloud 微服务也是基于 Spring Boot，因此，所有的 Java 工程师都有必要掌握好 Spring Boot。

## SpringBoot工程创建
### 第一种：在线创建
1. 打开创建地址：http://start.spring.io

2. 再页面填写工程信息及需要添加的依赖

   ![image-20200920115127677](https://gitee.com/JeanLv/study_image2/raw/master///image-20200920115127677.png)

3. 点击【GENERATE】生成工程并下载到本地

4. 本地的工程解压，使用IDE打开

### 第二种：IDE创建

#### IntelliJ IDEA的创建

1. 打开IntelliJ IDEA，点击【Create New Project】

2. 进行以下的创建步骤

![image-20200920115917527](https://gitee.com/JeanLv/study_image2/raw/master///image-20200920115917527.png)

![image-20200920120036242](https://gitee.com/JeanLv/study_image2/raw/master///image-20200920120036242.png)

![image-20200920120109323](https://gitee.com/JeanLv/study_image2/raw/master///image-20200920120109323.png)

![image-20200920120156082](https://gitee.com/JeanLv/study_image2/raw/master///image-20200920120156082.png)

#### STS

1. 首先右键单击，选择 New -> Spring Starter Project 

   ![img](https://gitee.com/JeanLv/study_image2/raw/master///1-6-20200920135017131.png)

2. 在页面中填入项目的相关信息

   ![img](https://gitee.com/JeanLv/study_image2/raw/master///1-7-20200920135054948.png)

### 第三章：改造一个maven工程来实现

1. 创建一个maven工程

2. 在pom.xml文件中添加SpringBoot需要的依赖，注意：Maven工程生成的依赖的调整

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.3.4.RELEASE</version>
       </parent>
       <artifactId>spring-boot-maven</artifactId>
       <groupId>org.example</groupId>
       <version>1.0-SNAPSHOT</version>
       <name>spring-boot-maven</name>
   
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
       </dependencies>
   
   </project>
   ```

3. 在src/main/java下创建Application.java启动类

   ```java
   package com.example.maven;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   /**
    * @author jinglv
    * @date 2020/09/20
    */
   @EnableAutoConfiguration
   @RestController
   public class Application {
       public static void main(String[] args) {
           SpringApplication.run(Application.class, args);
       }
   
       @GetMapping("/hello")
       public String hello() {
           return "Hello, SpringBoot";
       }
   
   }
   
   ```

   @EnableAutoConfiguration 注解表示开启自动化配置。

