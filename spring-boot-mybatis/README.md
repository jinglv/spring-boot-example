# MyBatis学习
MyBatis是现如今最流行的ORM框架之一。

## ORM框架
对象关系映射（Object Relational Mapping，ORM）模式是一种为了解决面向对象与关系数据库存在的互不匹配的现象的技术。简单的说，ORM是通过使用描述对象和数据库之间映射的元数据，将程序中的对象自动持久化到关系数据库中。

## MyBatis介绍
MyBatis是一款标准的ORM框架，被广泛的应用于各企业开发中。MyBatis最早是Apache的一个开源项目iBatis，2010年这个项目由Apache Software Foundation迁移到了Google Code，并且改名为MyBatis，2013年11月又迁移到 Github。从MyBatis的迁移史，也可以看出源码托管平台的发展史，GitHub目前已经成为世界上最大的开源软件托管平台。

MyBatis支持普通的SQL查询，存储过程和高级映射的优秀持久层框架。MyBatis消除了几乎所有的JDBC代码和参数的手工设置以及对结果集的检索封装。MaBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO（Plain Old Java Objects，普通的 Java 对象）映射成数据库中的记录。

作为一款使用广泛的开源软件，它的特点有哪些呢？

优点：
- SQL被统一提取出来，便于统一管理和优化
- SQL和代码解耦，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰、更易维护、更易单元测试
- 提供映射标签，支持对象与数据库的ORM字段关系映射
- 提供对象关系映射标签，支持对象关系组件维护
- 灵活书写动态SQL，支持各种条件来动态生成不同的SQL

缺点
- 编写SQL语句时工作量很大，尤其是字段多、关联表多时，更是如此
- SQL语句依赖于数据库，导致数据库移植性差

## MyBatis几个重要的概念
- **Mapper配置**可以使用基于XML的Mapper配置文件来实现，也可以使用基于Java注解的MyBatis注解来实现，甚至可以直接使用MyBatis提供的API来实现。
- **Mapper接口**是指自行定义的一个数据操作接口，类似于通常所说的DAO接口。早期的Mapper接口需要自定义去实现，现在MyBatis会自动为Mapper接口创建动态代理对象。Mapper接口的方法通常与Mapper配置文件中的select、insert、update、delete等XML结点存在一一对应关系。
- **Executor**，MyBatis中所有的Mapper语句的执行都是通过Executor进行的，Executor是MyBatis的一个核心接口。
- **SqlSession**，是MyBatis的关键对象，是执行持久化操作的独享，类似于JDBC中的Connection，SqlSession对象完全包含以数据库为背景的所有执行SQL操作的方法，它的底层封装了JDBC连接，可以用SqlSession实例来直接执行被映射的SQL语句。
- **SqlSessionFactory**，是MyBatis的关键对象，它是单个数据库映射关系经过编译后的内存镜像。SqlSessionFactory对象的实例可以通过SqlSessionFactoryBuilder对象类获得，而SqlSessionFactoryBuilder则可以从XML配置文件或一个预先定制的Configuration的实例构建出。

## MyBatis的工作流程如下

![img](https://gitee.com/JeanLv/study_image/raw/master///mybat-20200924114925143.png)

1. 加载Mapper配置SQL映射文件，或者是注解相关的SQL内容
2. 创建会话工厂，MyBatis通过读取配置文件的信息来构造出会话工厂（SqlSessionFactory）。
3. 创建会话。根据会话工厂，MyBatis就可以通过它来创建会话对象（SqlSession），会话对象是一个接口，该接口中包含了对数据库操作的增、删、改、查方法。
4. 创建执行器。因为会话对象本身不能直接操作数据库，所以它使用了一个叫数据库执行器（Executor）的接口来进行执行。
5. 封装SQL对象。在这一步，执行器将待处理的SQL信息封装到一个对象中（MappedStatement），该对象包括SQL语句、输入参数映射信息（Java简单类型、HashMap或POJO）和输出结果映射信息（Java简单类型、HashMap或POJO）。
6. 操作数据库。拥有了执行器和SQL信息封装对象就使用它们访问数据库了，最后返回操作结果，流程结果。



# MyBatis数据库的操作开发流程

## 准备及配置工作

1. 准备完成需要操作的数据

2. 项目引入依赖

   ```xml
   <dependency>
     <groupId>org.mybatis.spring.boot</groupId>
     <artifactId>mybatis-spring-boot-starter</artifactId>
     <version>${mybatis.version}</version>
   </dependency>
   
   <dependency>
     <groupId>mysql</groupId>
     <artifactId>mysql-connector-java</artifactId>
   </dependency>
   ```

   

3. 新增目录及配置文件

   - src/main/resources目录下新建如下

     - mybatis
       - mapper
     - mybatis-config.xml

     ![image-20200924121229735](https://gitee.com/JeanLv/study_image/raw/master///image-20200924121229735.png)

4. application.properties添加相关配置

   ```pro
   mybatis.config-location=classpath:mybatis/mybatis-config.xml
   mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
   mybatis.type-aliases-package=com.example.mybatis.mapper
   
   spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
   spring.datasource.username=root
   spring.datasource.password=root
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

   - mybatis.config-location，配置 mybatis-config.xml 路径，mybatis-config.xml 中配置 MyBatis 基础属性；
   - mybatis.mapper-locations，配置 Mapper 对应的 XML 文件路径；
   - mybatis.type-aliases-package，配置项目中实体类包路径；
   - spring.datasource.*，数据源配置。

   Spring Boot启动时数据源会自动注入到SqlSessionFactory中，使用SqlSessionFactory构建SqlSessionFactory，再自动注入到 Mapper中，最后我们直接使用Mapper即可。

5. 启动类添加Mapper包扫描

   - 在启动类中添加对 Mapper 包扫描 @MapperScan，Spring Boot 启动的时候会自动加载包路径下的 Mapper。

     ```java
     package com.example.mybatis;
     
     import org.mybatis.spring.annotation.MapperScan;
     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     
     /**
      * @author lvjing
      * @date 2020/09/24
      */
     @SpringBootApplication
     @MapperScan("com.example.mybatis.mapper")
     public class SpringBootMybatisApplication {
     
         public static void main(String[] args) {
             SpringApplication.run(SpringBootMybatisApplication.class, args);
         }
     
     }
     ```

     或者直接在 Mapper 类上面添加注解 @Mapper，建议使用上面那种，不然每个 mapper 加个注解也挺麻烦的。



## 示例流程

