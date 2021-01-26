# Druid介绍
Druid 是阿里巴巴开源平台上的一个项目，整个项目由数据库连接池、插件框架和 SQL 解析器组成，该项目主要是为了扩展 JDBC 的一些限制，可以让程序员实现一些特殊的需求，比如向密钥服务请求凭证、统计 SQL 信息、SQL 性能收集、SQL 注入检查、SQL 翻译等，程序员可以通过定制来实现自己需要的功能。

Druid 首先是一个数据库连接池，但它不仅仅是一个数据库连接池，还包含了一个 ProxyDriver，一系列内置的 JDBC 组件库，一个 SQL Parser。在 Java 的世界中 Druid 是监控做的最好的数据库连接池，在功能、性能、扩展性方面，也有不错的表现。

[Druid官方指南](https://github.com/alibaba/druid/wiki)



## Druid 可以做什么

- 替换其他 Java 连接池，Druid 提供了一个高效、功能强大、可扩展性好的数据库连接池。
- 可以监控数据库访问性能，Druid 内置提供了一个功能强大的 StatFilter 插件，能够详细统计 SQL 的执行性能，这对于线上分析数据库访问性能有很大帮助。
- 数据库密码加密。直接把数据库密码写在配置文件中，这是不好的行为，容易导致安全问题，DruidDruiver 和 DruidDataSource 都支持 PasswordCallback。
- SQL 执行日志，Druid 提供了不同的 LogFilter，能够支持 Common-Logging、Log4j 和 JdkLog，可以按需要选择相应的 LogFilter，监控应用的数据库访问情况。
- 扩展 JDBC，如果你要对 JDBC 层有编程的需求，可以通过 Druid 提供的 Filter 机制，很方便编写 JDBC 层的扩展插件。



# Spring Boot 集成 Druid

非常令人高兴的是，阿里为 Druid 也提供了 Spring Boot Starter 的支持。官网这样解释：Druid Spring Boot Starter 用于帮助你在 Spring Boot 项目中轻松集成 Druid 数据库连接池和监控。

Druid Spring Boot Starter 主要做了哪些事情呢？其实这个组件包很简单，主要提供了很多自动化的配置，按照 Spring Boot 的理念对很多内容进行了预配置，让我们在使用的时候更加的简单和方便。

## MyBatis 中使用 Druid 作为连接池
**引入依赖**
```xml
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.2.2</version>
        </dependency>
```
**application 配置**
Druid Spring Boot Starter 配置属性的名称完全遵照 Druid，可以通过 Spring Boot 配置文件来配置 Druid 数据库连接池和监控，如果没有配置则使用默认值。

注意：druid-spring-boot-starter 的版本为 1.1.10，会自动依赖 Druid 相关包，不需要编写DruidConfig配置类，可直接访问：http://localhost:port/druid，需要在配置文件中添加用户名和密码
```properties
# 实体类包路径
mybatis.type-aliases-package=com.example.mybatis.druid.entity
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
spring.datasource.username=root
spring.datasource.password=123123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# 初始化大小、最小、最大连接数
spring.datasource.druid.initial-size=3
spring.datasource.druid.min-idle=3
spring.datasource.druid.max-active=10
# 配置获取连接等待超时的时间
spring.datasource.druid.max-wait=60000
# 监控后台账号和密码
spring.datasource.druid.stat-view-servlet.login-username=admin
spring.datasource.druid.stat-view-servlet.login-password=admin
# 配置 StatFilter
spring.datasource.druid.filter.stat.log-slow-sql=true
spring.datasource.druid.filter.stat.slow-sql-millis=2000
```
增加了对 Druid 连接池的配置，以及 SQL 监控的配置，druid-spring-boot-starter 默认情况下开启 StatFilter 的监控功能。Druid Spring Boot Starter 不限于对以上配置属性提供支持，DruidDataSource 内提供 setter 方法的可配置属性都将被支持。

druid-spring-boot-starter 的版本在 1.1.10以上，需要增加DruidConfig配置类，配置文件中就不需要配置：监控后台账号和密码
```properties
# 实体类包路径
mybatis.type-aliases-package=com.example.mybatis.druid.entity
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
spring.datasource.username=root
spring.datasource.password=123123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# 初始化大小、最小、最大连接数
spring.datasource.druid.initial-size=3
spring.datasource.druid.min-idle=3
spring.datasource.druid.max-active=10
# 配置获取连接等待超时的时间
spring.datasource.druid.max-wait=60000
# 配置 StatFilter
spring.datasource.druid.filter.stat.log-slow-sql=true
spring.datasource.druid.filter.stat.slow-sql-millis=2000
```
DruidConfig.java
```java
package com.example.mybatis.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jingLv
 * @date 2021/01/21
 */
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * Druid监控
     * 1.配置一个管理后台的Servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> registrationBean() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet());
        bean.addUrlMappings("/druid/*");
        Map<String, String> initParams = new HashMap<>(16);
        // 设置用户名账户密码是固定的
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "123456");
        // 这个值为空就允许所有人访问
        initParams.put("allow", "");
        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * 2.配置一个web监控的Filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        bean.addUrlPatterns("/*");
        //可以过滤和排除哪些东西
        Map<String, String> initParams = new HashMap<>(16);
        //把不需要监控的过滤掉,这些不进行统计
        initParams.put("exclusions", "*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        return bean;
    }
}
```

配置完成后，直接启动项目访问地址：http://localhost:port/druid，就会出现 Druid 监控后台的登录页面，输入账户和密码后，就会进入首页。

![image-20210122132920384](https://gitee.com/JeanLv/study_image/raw/master///image-20210122132920384.png)

首页会展示项目使用的 JDK 版本、数据库驱动、JVM 相关统计信息。根据上面的菜单可以看出 Druid 的功能非常强大，支持数据源、SQL 监控、SQL 防火墙、URI 监控等很多功能。

在什么都没有操作时，点击“数据源”，会有如下展示：

![image-20210126105138201](https://gitee.com/JeanLv/study_image/raw/master///image-20210126105138201.png)

看到有一行红字，会以为是报错，不要慌，仔细看这个描述，这就是一个备注信息，提示用的，表明带 * 号的项是可能进行用户配置的。问题的原因是，项目中之前一直没有没有连接过一次数据库，没有使用过一次Database，也导致 database 的 init 方法没有初始化，所以这里是没有相关的信息的。之后我们进行功能操作，这时“数据源”的信息加载出来了，如下：

![image-20210126105444677](https://gitee.com/JeanLv/study_image/raw/master///image-20210126105444677-20210126105504165-20210126105509448.png)

我当时也以为是报错，去网上搜了一圈，乱七八糟的方案，很多都是一样的，明显都是乱拷一通。



我们这里重点介绍一下 SQL 监控，具体的展示信息如下：





这里的 SQL 监控会将项目中具体执行的 SQL 打印出来，展示此 SQL 执行了多少次、每次返回多少数据、执行的时间分布是什么。这些功能非常的实用，方便我们在实际生产中查找出慢 SQL，最后对 SQL 进行调优。

从这个例子可发现，使用 Spring Boot 集成 Druid 非常的简单，只需要添加依赖，简单配置就可以。