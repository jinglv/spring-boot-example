# MyBatis注解版
自从 Java 1.5 开始引入了注解，注解便被广泛地应用在了各种开源软件中，使用注解大大地降低了系统中的配置项，让编程变得更为优雅。MyBatis 也顺应潮流基于注解推出了 MyBatis 的注解版本，避免开发过程中频繁切换到 XML 或者 Java 代码中，从而让开发者使用 MyBatis 会有统一的开发体验。

因为最初设计时，MyBatis 是一个 XML 驱动的框架，配置信息是基于 XML 的，而且映射语句也是定义在 XML 中的，而到了 MyBatis 3，就有新选择了。MyBatis 3 构建在全面且强大的基于 Java 语言的配置 API 之上，这个配置 API 是基于 XML 的 MyBatis 配置的基础，也是新的基于注解配置的基础。注解提供了一种简单的方式来实现简单映射语句，而不会引入大量的开销。

注解版的使用方式和 XML 版本相同，只有在构建 SQL 方面有所区别，所以本课重点介绍两者之间的差异部分。

## 相关配置
注解版在 application.properties 只需要指明实体类的包路径即可，其他保持不变：
```properties
mybatis.type-aliases-package=package com.example.mybatis.annotation.model;
spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
spring.datasource.username=root
spring.datasource.password=123123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
logging.level.com.neo.mapper=debug
```

## 传统方式
使用注解版的 MyBatis 如何将参数传递到 SQL 中。

### 直接使用
```java
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
```
在 SQL 中使用 #{id} 来接收同名参数。

测试
```java
    @Test
    void getOne() {
        User user = userMapper.getOne(1L);
        System.out.println(user);
    }
```

### 使用 @Param
如果你的映射方法的形参有多个，这个注解使用在映射方法的参数上就能为它们取自定义名字。若不给出自定义名字，多参数则先以 "param" 作前缀，再加上它们的参数位置作为参数别名。例如，#{param1}、#{param2}，这个是默认值。如果注解是 @Param("person")，那么参数就会被命名为 #{person}。
```java
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
```
测试
```java
    @Test
    void getListUserByUserGender() {
        List<User> man = userMapper.getListUserByUserGender("MAN");
        man.forEach(System.out::println);
    }
```
### 使用 Map
需要传送多个参数时，可以考虑使用 Map：
```java
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
```
测试
```java
    @Test
    void getListUserByNameAndGender() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "admin");
        map.put("gender", "MAN");
        List<User> user = userMapper.getListUserByNameAndGender(map);
        user.forEach(System.out::println);
    }
```

### 使用对象
最常用的使用方式是直接使用对象：
```java
    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO users(user_name, pass_word, gender, nick_name) VALUES(#{userName}, #{passWord}, #{gender}, #{nickName})")
    int insertUser(User user);
```
在执行时，系统会自动读取对象的属性并值赋值到同名的 #{xxx} 中。

测试
```java
    @Test
    void insertUser() {
        User user = new User();
        user.setUserName("huahua").setPassWord("123456").setGender(GenderEnum.WOMAN).setNickName("花妹");
        int i = userMapper.insertUser(user);
        System.out.println(i);
    }
```

## 注解介绍
**注解版最大的特点是具体的 SQL 文件需要写在 Mapper 类中，取消了 Mapper 的 XML 配置**。

### @Select 注解
@Select 主要在查询的时候使用，查询类的注解，所有的查询均使用这个，具体如下：
```java
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
```
### @Insert 注解
@Insert，插入数据库时使用，直接传入实体类会自动解析属性到对应的值，示例如下:
```java
    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO users(user_name, pass_word, gender, nick_name) VALUES(#{userName}, #{passWord}, #{gender}, #{nickName})")
    int insertUser(User user);
```
### @Update 注解
@Update，所有的更新操作 SQL 都可以使用 @Update。
```java
    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 影响行数
     */
    @Update("UPDATE users SET user_name=#{userName},nick_name=#{nickName} WHERE id =#{id}")
    int updateUser(User user);
```
### @Delete 注解
@Delete 处理数据删除。
```java
    /**
     * 根据id删除用户信息
     *
     * @param id 表的主键
     * @return 影响行数
     */
    @Delete("DELETE FROM users WHERE id =#{id}")
    int deleteUser(Long id);
```
### @Results 和 @Result 注解
这两个注解配合来使用，主要作用是将数据库中查询到的数值转化为具体的字段，修饰返回的结果集，关联实体类属性和数据库字段一一对应，如果实体类属性和数据库属性名保持一致，就不需要这个属性来修饰。示例如下：
```java
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
```

## 动态 SQL
MyBatis 最大的特点是可以灵活的支持动态 SQL，在注解版中提供了两种方式来支持，第一种是使用注解来实现，另一种是提供 SQL 类来支持。
### 使用注解来实现
用 script 标签包围，然后像 XML 语法一样书写：
```java
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
```
这种方式就是注解 + XML 的混合使用方式，既有 XML 灵活又有注解的方便，但也有一个缺点需要在 Java 代码中拼接 XML 语法很不方便，因此 MyBatis 又提供了一种更优雅的使用方式来支持动态构建 SQL。

### 使用 SQL 构建类来支持
以分页为例进行演示，首先定义一个 UserSql 类，提供方法拼接需要执行的 SQL：
```java
package com.example.mybatis.annotation.mapper;

import com.example.mybatis.annotation.param.UserParam;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * SQL构建动态SQL
 *
 * @author jingLv
 * @date 2021/01/15
 */
public class UserSql {
    public String getList(UserParam userParam) {
        StringBuilder sql = new StringBuilder("select id, user_name as userName, pass_word as passWord, gender, nick_name as nickName");
        sql.append(" from users where 1=1 ");
        if (userParam != null) {
            if (!ObjectUtils.isEmpty(userParam.getUserName())) {
                sql.append(" and user_name = #{userName}");
            }
            if (!ObjectUtils.isEmpty(userParam.getGender())) {
                sql.append(" and gender = #{gender}");
            }
        }
        sql.append(" order by id desc");
        sql.append(" limit ").append(Objects.requireNonNull(userParam).getBeginLine()).append(",").append(userParam.getPageSize());
        return sql.toString();
    }

    public String getCount(UserParam userParam) {
        String sql = new SQL() {{
            SELECT("count(1)");
            FROM("users");
            if (!ObjectUtils.isEmpty(userParam.getUserName())) {
                WHERE("user_name = #{userName}");
            }
            if (!ObjectUtils.isEmpty(userParam.getGender())) {
                WHERE("gender = #{gender}");
            }
            //从这个toString可以看出，其内部使用高效的StringBuilder实现SQL拼接
        }}.toString();
        return sql;
    }
}
```
可以看出 UserSql 中有一个方法 getList，使用 StringBuilder 对 SQL 进行拼接，通过 if 判断来动态构建 SQL，最后方法返回需要执行的 SQL 语句。

接下来只需要在 Mapper 中引入这个类和方法即可。
```java
    /**
     * 分页查询数据
     *
     * @param userParam user的分类
     * @return 返回数据集合
     */
    @SelectProvider(type = UserSql.class, method = "getList")
    List<User> getList(UserParam userParam);

    /**
     * 统计出查询结果的总数
     *
     * @param userParam user的分类
     * @return 返回总数
     */
    @SelectProvider(type = UserSql.class, method = "getCount")
    int getCount(UserParam userParam);

```
- type：动态生成 SQL 的类
- method：类中具体的方法名

相对于 @SelectProvider 提供查询 SQL 方法导入，还有 @InsertProvider、@UpdateProvider、@DeleteProvider 提供给插入、更新、删除的时候使用。

测试代码：
```java
    @Test
    void getList() {
        UserParam userParam = new UserParam();
        userParam.setGender("WOMAN");
        userParam.setCurrentPage(1);

        List<User> users = userMapper.getList(userParam);
        long count = userMapper.getCount(userParam);
        Page<User> page = new Page<>(userParam, count, users);
        System.out.println(page);
    }

```

## 结构化 SQL
可能你会觉得这样拼接 SQL 很麻烦，SQL 语句太复杂也比较乱，MyBatis 给我们提供了一种升级的方案：结构化 SQL。
```java
    /**
     * 结构化 SQL
     *
     * @param userParam
     * @return
     */
    public String getCount(UserParam userParam) {
        String sql = new SQL() {{
            SELECT("count(1)");
            FROM("users");
            if (!ObjectUtils.isEmpty(userParam.getUserName())) {
                WHERE("user_name = #{userName}");
            }
            if (!ObjectUtils.isEmpty(userParam.getGender())) {
                WHERE("gender = #{gender}");
            }
            //从这个toString可以看出，其内部使用高效的StringBuilder实现SQL拼接
        }}.toString();
        return sql;
    }
```
- SELECT 表示要查询的字段，可以写多行，多行的 SELECT 会智能地进行合并而不会重复。
- FROM 和 WHERE 跟 SELECT 一样，可以写多个参数，也可以在多行重复使用，最终会智能合并而不会报错。这样语句适用于写很长的 SQL，且能够保证 SQL 结构清楚，便于维护、可读性高。

更多结构化的 SQL 语法请参考[SQL语句构建器类](https://mybatis.org/mybatis-3/zh/statement-builders.html)。

