# MyBatis 注解版多数据源处理
注解版的多数据源使用和 XML 版本的多数据源基本一致。

1. 配置文件，需要配置两个不同的数据源
    ```properties
        logging.level.com.example.mybatis.annotation.multi.mapper=debug
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
    
2. 数据源配置类，创建config包，分别创建DataSourceOneConfig.java和DataSourceTwoConfig.java
  
    ![image-20210118172257720](https://gitee.com/JeanLv/study_image/raw/master///image-20210118172257720.png)
    
    **DataSourceOneConfig.java**
    ```java
    package com.example.mybatis.annotaion.multi.config;
    
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
    import org.springframework.jdbc.datasource.DataSourceTransactionManager;
    
    import javax.sql.DataSource;
    
    /**
     * @author jingLv
     * @date 2021/01/18
     */
    @Configuration
    @MapperScan(basePackages = "com.example.mybatis.annotaion.multi.mapper.one", sqlSessionTemplateRef = "testOneSqlSessionTemplate")
    public class DataSourceOneConfig {
        @Bean(name = "testOneDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.one")
        @Primary
        public DataSource testDataSource() {
            return DataSourceBuilder.create().build();
        }
    
        @Bean(name = "testOneSqlSessionFactory")
        @Primary
        public SqlSessionFactory testSqlSessionFactory(@Qualifier("testOneDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            return bean.getObject();
        }
    
        @Bean(name = "testOneTransactionManager")
        @Primary
        public DataSourceTransactionManager testTransactionManager(@Qualifier("testOneDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    
        @Bean(name = "testOneSqlSessionTemplate")
        @Primary
        public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("testOneSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
   }
   ```
   
   **DataSourceTwoConfig.java**，DataSource2 配置和 DataSource1 配置基本相同，只是去掉了 @Primary
   ```java
    package com.example.mybatis.annotaion.multi.config;
    
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
    import org.springframework.jdbc.datasource.DataSourceTransactionManager;
    
    import javax.sql.DataSource;
    
    /**
     * @author jingLv
     * @date 2021/01/18
     */
    @Configuration
    @MapperScan(basePackages = "com.example.mybatis.annotaion.multi.mapper.two", sqlSessionTemplateRef = "testTwoSqlSessionTemplate")
    public class DataSourceTwoConfig {
        @Bean(name = "testTwoDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.two")
        public DataSource testDataSource() {
            return DataSourceBuilder.create().build();
        }
    
        @Bean(name = "testTwoSqlSessionFactory")
        @Primary
        public SqlSessionFactory testSqlSessionFactory(@Qualifier("testTwoDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            return bean.getObject();
        }
    
        @Bean(name = "testTwoTransactionManager")
        @Primary
        public DataSourceTransactionManager testTransactionManager(@Qualifier("testTwoDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    
        @Bean(name = "testTwoSqlSessionTemplate")
        @Primary
        public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("testTwoSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }
   ```
   
3. 在Mapper包下创建one和two两个包，分别作为两个不同数据源的Mapper来使用

    ![image-20210118172210145](https://gitee.com/JeanLv/study_image/raw/master///image-20210118172210145.png)

    UserOneMapper.java和UserTwoMapper.java内容是一样的

    ```java
    package com.example.mybatis.annotaion.multi.mapper.one;
    
    
    import com.example.mybatis.annotaion.multi.enums.GenderEnum;
    import com.example.mybatis.annotaion.multi.model.User;
    import org.apache.ibatis.annotations.*;
    
    import java.util.List;
    import java.util.Map;
    
    /**
     * @author jingLv
     * @date 2020/09/24
     */
    public interface UserOneMapper {
        /**
         * 获取所有的用户数据
         *
         * @return 返回所有用户列表
         */
        @Select("SELECT * FROM users")
        @Results({
                @Result(property = "userName", column = "user_name"),
                @Result(property = "passWord", column = "pass_word"),
                @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
                @Result(property = "nickName", column = "nick_name")
        })
        List<User> getAllUsers();
    
        /**
         * 根据id查询单条用户数据
         *
         * @param id 传入id
         * @return 返回用户数据
         */
        @Select("SELECT * FROM users WHERE id=#{id}")
        @Results({
                @Result(property = "userName", column = "user_name"),
                @Result(property = "passWord", column = "pass_word"),
                @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
                @Result(property = "nickName", column = "nick_name")
        })
        User getOne(Long id);
    
        /**
         * 根据性别查询用户
         * Param("表的字段名")
         *
         * @param gender 性别
         * @return 返回用户数据
         */
        @Select("SELECT * FROM users WHERE gender=#{gender}")
        @Results({
                @Result(property = "userName", column = "user_name"),
                @Result(property = "passWord", column = "pass_word"),
                @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
                @Result(property = "nickName", column = "nick_name")
        })
        List<User> getListUserByUserGender(@Param("gender") String gender);
    
        /**
         * 根据用户名和性别查询用户
         *
         * @param map map
         * @return 返回用户数据
         */
        @Select("SELECT * FROM users WHERE user_name=#{userName} AND gender = #{gender}")
        @Results({
                @Result(property = "userName", column = "user_name"),
                @Result(property = "passWord", column = "pass_word"),
                @Result(property = "gender", column = "gender", javaType = GenderEnum.class),
                @Result(property = "nickName", column = "nick_name")
        })
        List<User> getListUserByNameAndGender(Map<String, Object> map);
    
        /**
         * 新增用户
         *
         * @param user 用户信息
         * @return 影响行数
         */
        @Insert("INSERT INTO users(user_name, pass_word, gender, nick_name) VALUES(#{userName}, #{passWord}, #{gender}, #{nickName})")
        int insertUser(User user);
    
        /**
         * 修改用户信息
         *
         * @param user 用户信息
         * @return 影响行数
         */
        @Update("UPDATE users SET user_name=#{userName},nick_name=#{nickName} WHERE id =#{id}")
        int update(User user);
    
        /**
         * 修改用户信息
         *
         * @param user 用户信息
         * @return 影响行数
         */
        @Update({"<script> ",
                "update users ",
                "<set>",
                " <if test='user_name != null'>userName=#{userName},</if>",
                " <if test='nick_name != null'>nick_name=#{nickName},</if>",
                " </set> ",
                "where id=#{id} ",
                "</script>"})
        int updateUser(User user);
    
        /**
         * 根据id删除用户信息
         *
         * @param id 表的主键
         * @return 影响行数
         */
        @Delete("DELETE FROM users WHERE id =#{id}")
        int deleteUser(Long id);
    }
    ```

4. 测试，注入不同的Mapper即可
    ```java 
    package com.example.mybatis.annotaion.multi;
    
    
    import com.example.mybatis.annotaion.multi.mapper.one.UserOneMapper;
    import com.example.mybatis.annotaion.multi.mapper.two.UserTwoMapper;
    import com.example.mybatis.annotaion.multi.model.User;
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

