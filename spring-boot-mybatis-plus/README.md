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

   1. 数据表结构

      ```sql
   -- 创建user表
      DROP TABLE IF EXISTS users;
      CREATE TABLE users
      (
          id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
          job_id      BIGINT      DEFAULT NULL COMMENT '工作id',
          user_name   VARCHAR(32) DEFAULT NULL COMMENT '用户名',
          pass_word   VARCHAR(32) DEFAULT NULL COMMENT '密码',
          age         INT(11)     DEFAULT NULL COMMENT '年龄',
          email       VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
          manager_id  BIGINT(20)  DEFAULT NULL COMMENT '直属上级id',
          create_time DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
          update_time TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
      ) ENGINE = InnoDB
        AUTO_INCREMENT = 1
        DEFAULT CHARSET = utf8 COMMENT '用户表';
      
      -- 数据初始化
      INSERT INTO users (job_id, user_name, pass_word, age, email, manager_id)
      VALUES (1000101, '大Boss', 'admin', 40, 'boss@baomidou.com', NULL),
             (1000102, '王天风', '123456', 25, 'wtf@baomidou.com', 1000101),
             (1000103, '李艺伟', '123123', 28, 'lyw@baomidou.com', 1000102),
             (1000104, '张雨琪', '123123', 31, 'zjq@baomidou.com', 1000102),
             (1000105, '刘红雨', '123123', 32, 'lhm@baomidou.com', 1000102);
      ```
   
      
   
   2. 开发实体类
   
      ```java
   package com.example.mybatis.plus.entity;
      
   import lombok.AllArgsConstructor;
      import lombok.Data;
   import lombok.NoArgsConstructor;
      import lombok.ToString;
      import lombok.experimental.Accessors;
      
      /**
       * Accessors(chain = true) 开启链式调用
       * <p>
       * 注意：如果不加对应的注解，则实体的表名和字段必须要与表的名称和列名必须一致
       *
       * @author jingLv
       * @date 2020/09/28
       */
      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      @ToString
   @Accessors(chain = true)
      public class User {
       private Long id;
          private Long jobId;
          private String userName;
          private String passWord;
          private Integer age;
          private String email;
          private Long managerId;
      }
      ```
   
      
   
   3. 开发mapper通用实现
   
      ```java
      package com.example.mybatis.plus.dao;
      
      import com.baomidou.mybatisplus.core.mapper.BaseMapper;
      import com.example.mybatis.plus.entity.User;
      
      /**
       * 使用MyBatis-Plus增强接口
       *
       * @author jingLv
       * @date 2020/09/28
       */
      public interface UserDao extends BaseMapper<User> {
      }
      ```

   4. 测试
   
      ```java
      package com.example.mybatis.plus.dao;
      
      import com.example.mybatis.plus.entity.User;
      import org.junit.jupiter.api.Test;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.context.SpringBootTest;
      
      import java.util.List;
      
      /**
       * @author jingLv
       * @date 2020/09/28
       */
      @SpringBootTest
      class UserDaoTest {
          @Autowired
          private UserDao userDao;
      
          @Test
          void testFindAll() {
              List<User> users = userDao.selectList(null);
              users.forEach(System.out::println);
              users.forEach(user -> System.out.println("user=" + user));
          }
      
      }
      ```
   
      

## 常用注解

- **@TableName**  
- **@TableId**
- **@TableField**

### @TableNam注解

- **描述:**用来将实体对象与数据库表名完成映射
- **修饰范围:** 用在类上
- **常见属性:**
  - **value:** String类型,指定映射的表名
  - **resultMap:**String类型,用来指定XML配置中resultMap的id值



### @TableId注解

- **描述**：主键注解

- **修饰范围:**用在属性上

- **常见属性:**

  - **value:** String类型,指定实体类中与表中对应的主键列名

  - **type:**  枚举类型,指定主键生成类型

    - IdType

      | 值                | 描述                                                         |
      | ----------------- | ------------------------------------------------------------ |
      | AUTO              | 数据库ID自增                                                 |
      | NONE              | 无状态，该类型为未设置主键类型（注解里等于跟随全局，全局里约等于INPUT） |
      | INPUT             | insert前自行set主键值                                        |
      | ASSIGN_ID         | 分配ID（主键为Number(Long和Integer)或String）(since3.3.0)，使用接口IdentifierGenerator的方法nextId（默认实现类为DefaultIdentifierGenerator雪花算法） |
      | ASSIGN_UUID       | 分配UUID，主键类型为String(since3.3.0)，使用接口IdentifierGenerator的方法nextUUID（默认default方法） |
      | ~~ID_WORKER~~     | 分布式全局唯一ID 长整型类型（please use ASSIGN_ID）          |
      | ~~UUID~~          | 32位UUID字符串（please use ASSIGN_ID）                       |
      | ~~ID_WORKER_STR~~ | 分布式全局唯一ID字符串类型（please use ASSIGN_ID）           |



### @TableField

- **描述**：字段注解(非主键)
- **修饰范围:**用在属性上
- **常用属性:**
  - **value:**	String类型,用来指定对应的数据库表中的字段名
  - **el:**	String类型,映射为原生 #{ ... } 逻辑,相当于写在 xml 里的 #{ ... } 部分，别名， 3.0不存在
  - exist	boolean是否为数据库表字段 true代表是数据库字段,false代表不是

```java
package com.example.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Accessors(chain = true) 开启链式调用
 * <p>
 * 注意：如果不加对应的注解，则实体的表名和字段必须要与表的名称和列名必须一致
 * <p>
 * 默认将类名作为表名
 *
 * @author jingLv
 * @date 2020/09/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("users")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("job_id")
    private Long jobId;
    @TableField("user_name")
    private String userName;
    @TableField("pass_word")
    private String passWord;
    private Integer age;
    private String email;
    @TableField("manager_id")
    private Long managerId;

    @TableField(exist = false)
    private String description;
}

```

## 常用方法
### 查询方法

1. 基本查询

   - selectById：根据ID查询

     ```
     @Test
     void testFindOne() {
       User user = userDao.selectById(2);
       System.out.println(user);
     }
     ```

     

   - selectBatchIds：根据ID批量查询

     ```
     @Test
     void testFindIds() {
       List<Integer> ids = Arrays.asList(1, 2, 3);
       List<User> users = userDao.selectBatchIds(ids);
       users.forEach(System.out::println);
     }
     ```

   - selectByMap：根据columnMap(列名)的条件

     ```
     @Test
     void testFindByMap() {
       Map<String, Object> columnMap = new HashMap<>();
       // key是数据库中的列名，不是User实体类的名称
       columnMap.put("user_name", "王天风");
       columnMap.put("age", 25);
       List<User> users = userDao.selectByMap(columnMap);
       users.forEach(System.out::println);
     }
     ```

     

2. 条件构造器查询

   1. 查询条件构造的两种方式
      - QueryWrapper<User> queryWrapper = new QueryWrapper<>();
      - QueryWrapper<User> queryWrapper = Wrappers.query();

   - selectList：根据实体类条件，查询全部记录

     - 注意：传入的queryWrapper为null，则查询所有数据

     ```
      /**
      * 查询所有数据
      */
      @Test
      void testQueryAll() {
        List<User> users = userDao.selectList(null);
        users.forEach(user -> System.out.println("users=" + user));
      }
      
      /**
      * 名字中包含雨并且年龄小于40
      * user_name like '%雨%' and age<40
      */
      @Test
      void testQueryOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.like("user_name", "雨").lt("age", 40);
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
      }
      
      /**
      * 名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
      * user_name like '%雨%' and age between 20 and 40 and email is not null
      */
      @Test
      void testQueryTwo() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.like("user_name", "雨").between("age", 20, 40).isNotNull("email");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
      }
      
      /**
      * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
      * user_name like '王%' or age>=25 order by age desc,id asc
      */
      @Test
      void testQueryThree() {
      QueryWrapper<User> queryWrapper = new QueryWrapper<>();
      // 构造条件
      queryWrapper.likeRight("user_name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("id");
      List<User> users = userDao.selectList(queryWrapper);
      users.forEach(user -> System.out.println("users=" + user));
      }
      
      /**
      * 创建日期为2019年2月14日并且直属上级为名字为王姓
      * date_format(create_time,'%Y-%m-%d')='2020-09-29' and manager_id in (select job_id from users where user_name like '王%')
      * date_format(create_time,'%Y-%m-%d')='2020-09-29') 这种方式容易造成SQL注入
      */
      @Test
      void testQueryFour() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}", "2020-09-29")
        			.inSql("manager_id", "select job_id from users where user_name like '王%'");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
      }
      
      /**
      * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
      * user_name like '王%' or (age<40 and age>20 and email is not null)
      */
      @Test
      void testQuerySix() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.likeRight("user_name", "王").or(wq -> wq.lt("age", 40).gt("age", 20).isNotNull("email"));
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
      }
      
      /**
      * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
      * user_name like '王%' or (age<40 and age>20 and email is not null)
      * and优先级高于or
      */
      @Test
      void testQuerySeven() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 构造条件
        queryWrapper.nested(wq -> wq.lt("age", 40).or().isNotNull("email"))
        			.likeRight("user_name", "王");
        List<User> users = userDao.selectList(queryWrapper);
        users.forEach(user -> System.out.println("users=" + user));
     }
     
     /**
     * 年龄为30、31、34、35
     * age in (30、31、34、35)
     */
     @Test
     void testQueryEight() {
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       // 构造条件
       queryWrapper.in("age", Arrays.asList(30, 31, 34, 35));
       List<User> users = userDao.selectList(queryWrapper);
       users.forEach(user -> System.out.println("users=" + user));
     }
     
     /**
     * 只返回满足条件的其中一条语句即可
     * limit 1
     * <p>
     * last:无视优化规则直接拼接到sql的最后
     * 注意事项：只能调用一次，多次调用以最后一次为准，有sql注入的风险，请谨慎使用
     */
     @Test
     void testQueryNine() {
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       // 构造条件
       queryWrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
       List<User> users = userDao.selectList(queryWrapper);
       users.forEach(user -> System.out.println("users=" + user));
     }
     ```

3. SELECT不列出全部字段

   - queryWrapper.select("column1", "column2")....

   ```
   /**
   * 名字中包含雨并且年龄小于40
   * select job_id,user_name from users where user_name like '%雨%' and age<40
   */
   @Test
   void testQueryFieldOne() {
     QueryWrapper<User> queryWrapper = new QueryWrapper<>();
     // 构造条件
     queryWrapper.select("job_id", "user_name").like("user_name", "雨").lt("age", 40);
     List<User> users = userDao.selectList(queryWrapper);
     users.forEach(user -> System.out.println("users=" + user));
   }
   
   /**
   * 名字中包含雨并且年龄小于40
   * select id,user_name,pass_word, age,email from users where user_name like '%雨%' and age<40
   * 字段较多的情况,直接传实体
   * select可以写在语句的后面
   */
   @Test
   void testQueryFieldTwo() {
     QueryWrapper<User> queryWrapper = new QueryWrapper<>();
     // 构造条件
     queryWrapper.like("user_name", "雨").lt("age", 40)
     .select(User.class, info -> !info.getColumn().equals("manager_id") &&
     !info.getColumn().equals("job_id"));
     List<User> users = userDao.selectList(queryWrapper);
     users.forEach(user -> System.out.println("users=" + user));
   }
   ```

   

4. 条件构造器中condition作用

   - condition执行条件:控制条件是否加入到查询条件中

     ```
     public void condition(String userName, String email) {
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       // 这种写法不简洁
       /*if (StringUtils.isNotBlank(userName)) {
       queryWrapper.like("user_name", userName);
       }
       if (StringUtils.isNotBlank(email)) {
       queryWrapper.like("email", email);
       }*/
       // 优化写法
       queryWrapper.like(StringUtils.isNotBlank(userName), "user_name", userName)
       .like(StringUtils.isNotBlank(email), "email", email);
     
       List<User> users = userDao.selectList(queryWrapper);
       users.forEach(user -> System.out.println("users=" + user));
     }
     
     @Test
     void testCondition() {
       // 模拟前台传来两个参数
       String name = "王";
       String email = "";
       condition(name, email);
     }
     ```

     

### 新增方法
### 更新方法
### 删除方法

