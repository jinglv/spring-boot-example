# MyBatis XML配置版多数据源处理
1. 配置文件，需要配置两个不同的数据源
    ```properties
    mybatis.config-location=classpath:mybatis/mybatis-config.xml
    logging.level.com.example.mybatis.multi.mapper=debug
    # 数据库1
    spring.datasource.one.jdbc-url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
    spring.datasource.one.username=root
    spring.datasource.one.password=123123
    spring.datasource.one.driver-class-name=com.mysql.cj.jdbc.Driver
    # 数据库2
    spring.datasource.two.jdbc-url=jdbc:mysql://localhost:3306/test_tp?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
    spring.datasource.two.username=root
    spring.datasource.two.password=123123
    spring.datasource.two.driver-class-name=com.mysql.cj.jdbc.Driver
    ```
    注意:
    - 需要提前在 test1 和 test2 库中创建好 User 表结构。
    - spring.datasource.url 数据库的 JDBC URL。
    - spring.datasource.jdbc-url 用来重写自定义连接池，多数据源在定义数据库url需要使用jdbc-url
    
    数据源：
    - 第一个数据源以 spring.datasource.one.* 为前缀连接数据库 test
    - 第二个数据源以 spring.datasource.two.* 为前缀连接数据库 test_tp
    
2. 在resources/mybatis/mapper下，分别创建两个目录，创建各自的*Mapper.xml文件，对应的mapper包下分别创建两个包，创建各自的*Mapper.java

    ![image-20210115172521830](https://gitee.com/JeanLv/study_image/raw/master///image-20210115172521830.png)

3. 数据源配置类，创建config包，分别创建DataSourceOneConfig.java和DataSourceTwoConfig.java
    
    ![image-20210115173417417](https://gitee.com/JeanLv/study_image/raw/master///image-20210115173417417.png)
    
    **DataSourceOneConfig.java**
    
    ```java
    package com.example.mybatis.multi.config;
    
    import org.apache.ibatis.session.SqlSessionFactory;
    import org.mybatis.spring.SqlSessionFactoryBean;
    import org.mybatis.spring.SqlSessionTemplate;
    import org.mybatis.spring.annotation.MapperScan;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.boot.jdbc.DataSourceBuilder;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Primary;
    import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
    import org.springframework.jdbc.datasource.DataSourceTransactionManager;
    
    import javax.sql.DataSource;
    
    /**
     * @author jingLv
     * @date 2021/01/15
     */
    @Configuration
    @MapperScan(basePackages = "com.example.mybatis.multi.mapper.one", sqlSessionTemplateRef = "oneSqlSessionTemplate")
    public class DataSourceOneConfig {
        @Bean(name = "oneDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.one")
        @Primary
        public DataSource testDataSource() {
            return DataSourceBuilder.create().build();
        }
    
        @Bean(name = "oneSqlSessionFactory")
        @Primary
        public SqlSessionFactory testSqlSessionFactory(@Qualifier("oneDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/one/*.xml"));
            return bean.getObject();
        }
    
        @Bean(name = "oneTransactionManager")
        @Primary
        public DataSourceTransactionManager testTransactionManager(@Qualifier("oneDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    
        @Bean(name = "oneSqlSessionTemplate")
        @Primary
        public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("oneSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }
    ```
    注意，在多数据源中只能指定一个 `@Primary` 作为默认的数据源使用。
    步骤分解查看理解：
    1. 加载配置的数据源
        ```java
            /**
             * 加载配置的数据源
             *
             * @return
             */
            @Bean(name = "oneDataSource")
            @ConfigurationProperties(prefix = "spring.datasource.one")
            @Primary
            public DataSource testDataSource() {
                return DataSourceBuilder.create().build();
            }
        ```
    2. 根据创建的数据源，构建对应的 SqlSessionFactory，代码中需要指明需要加载的 Mapper xml 文件
        ```java
            /**
             * 根据创建的数据源，构建对应的 SqlSessionFactory
             * 代码中需要指明需要加载的 Mapper xml 文件
             *
             * @param dataSource
             * @return
             * @throws Exception
             */
            @Bean(name = "oneSqlSessionFactory")
            @Primary
            public SqlSessionFactory testSqlSessionFactory(@Qualifier("oneDataSource") DataSource dataSource) throws Exception {
                SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
                bean.setDataSource(dataSource);
                bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/one/*.xml"));
                return bean.getObject();
            }
        ```
    3. 将数据源添加到事务中
        ```java
            /**
             * 将数据源添加到事务中
             *
             * @param dataSource
             * @return
             */
            @Bean(name = "oneTransactionManager")
            @Primary
            public DataSourceTransactionManager testTransactionManager(@Qualifier("oneDataSource") DataSource dataSource) {
                return new DataSourceTransactionManager(dataSource);
            }
        ```
    4. 将上面创建的 SqlSessionFactory 注入，创建我们在 Mapper 中需要使用的 SqlSessionTemplate
        ```java
            /**
             * 将上面创建的 SqlSessionFactory 注入，创建我们在 Mapper 中需要使用的 SqlSessionTemplate
             *
             * @param sqlSessionFactory
             * @return
             * @throws Exception
             */
            @Bean(name = "oneSqlSessionTemplate")
            @Primary
            public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("oneSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
                return new SqlSessionTemplate(sqlSessionFactory);
            }
        ```
    5. 将创建的 SqlSessionTemplate 注入到对应的 Mapper 包路径下，这样这个包下面的 Mapper 都会使用第一个数据源来进行数据库操作
        ```java
        /**
         *
         * @author jingLv
         * @date 2021/01/15
         */
        @Configuration
        @MapperScan(basePackages = "com.example.mybatis.multi.mapper.one", sqlSessionTemplateRef = "oneSqlSessionTemplate")
        public class DataSourceOneConfig {
            ......
    }
        ```
    - basePackages 指明 Mapper 地址。
    - sqlSessionTemplateRef 指定 Mapper 路径下注入的 sqlSessionTemplate。
    
    
    
    **DataSourceTwoConfig.java**
    
    
    
    DataSourceTwoConfig 的配置和上面类似，方法上需要去掉 `@Primary` 注解，替换对应的数据源和 Mapper 路径即可。
    
    ```java
    package com.example.mybatis.multi.config;
    
    import org.apache.ibatis.session.SqlSessionFactory;
    import org.mybatis.spring.SqlSessionFactoryBean;
    import org.mybatis.spring.SqlSessionTemplate;
    import org.mybatis.spring.annotation.MapperScan;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.boot.jdbc.DataSourceBuilder;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
    import org.springframework.jdbc.datasource.DataSourceTransactionManager;
    
    import javax.sql.DataSource;
    
    /**
     * @author jingLv
     * @date 2021/01/15
     */
    @Configuration
    @MapperScan(basePackages = "com.example.mybatis.multi.mapper.two", sqlSessionTemplateRef = "twoSqlSessionTemplate")
    public class DataSourceTwoConfig {
        @Bean(name = "twoDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.two")
        public DataSource testDataSource() {
            return DataSourceBuilder.create().build();
        }
    
        @Bean(name = "twoSqlSessionFactory")
        public SqlSessionFactory testSqlSessionFactory(@Qualifier("twoDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/two/*.xml"));
            return bean.getObject();
        }
    
        @Bean(name = "twoTransactionManager")
        public DataSourceTransactionManager testTransactionManager(@Qualifier("twoDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    
        @Bean(name = "twoSqlSessionTemplate")
        public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("twoSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }
    
    ```
    
    - 从上面的步骤我们可以总结出来，创建多数据源的过程就是：首先创建 DataSource，注入到 SqlSessionFactory 中，再创建事务，将 SqlSessionFactory 注入到创建的 SqlSessionTemplate 中，最后将 SqlSessionTemplate 注入到对应的 Mapper 包路径下。其中需要指定分库的 Mapper 包路径。
    - 注意，在多数据源的情况下，我们不需要在启动类添加：@MapperScan("com.xxx.mapper") 的注解。
    
4. 测试，配置好多数据源之后，在项目中想使用哪个数据源就把对应数据源注入到类中使用即可

    ```java
    package com.example.mybatis.multi;
    
    import com.example.mybatis.multi.mapper.one.UserOneMapper;
    import com.example.mybatis.multi.mapper.two.UserTwoMapper;
    import com.example.mybatis.multi.model.User;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.junit.jupiter.SpringExtension;
    
    import java.util.List;
    
    /**
     * @author jingLv
     * @date 2021/01/15
     */
    @ExtendWith(SpringExtension.class)
    @SpringBootTest
    public class UserMapperTest {
        @Autowired
        private UserOneMapper userOneMapper;
        @Autowired
        private UserTwoMapper userTwoMapper;
    
        @Test
        void getUser() {
            List<User> oneUsers = userOneMapper.getAllUsers();
            List<User> twoUsers = userTwoMapper.getAllUsers();
            oneUsers.forEach(System.out::println);
            twoUsers.forEach(System.out::println);
        }
    }
    ```

    