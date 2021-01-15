# MyBatis XML版
MyBatis是现如今最流行的ORM框架之一。

[MyBatis官方网站](https://mybatis.org/mybatis-3/zh/index.html)

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

创建数据库，并创建数据表
```sql
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    user_name   VARCHAR(32) DEFAULT NULL COMMENT '用户名',
    pass_word   VARCHAR(32) DEFAULT NULL COMMENT '密码',
    gender      VARCHAR(64) DEFAULT 'UNKNOWN'         NOT NULL COMMENT '性别 MALE:男,FEMALE:女,UNKNOWN:未知',
    nick_name   VARCHAR(32) DEFAULT NULL,
    create_time DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT '用户表';

INSERT INTO users(user_name, pass_word, gender, nick_name)
VALUES ('admin', '123123', 'MAN', '首脑');
```

2. 项目引入依赖

   ```
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
   mybatis.type-aliases-package=com.example.mybatis.model
   
   spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
   spring.datasource.username=root
   spring.datasource.password=123123
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

   - mybatis.config-location，配置mybatis-config.xml路径，mybatis-config.xml中配置MyBatis基础属性；
   - mybatis.mapper-locations，配置Mapper对应的XML文件路径；
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
基础的配置已完成，接下是进行具体的流程代码及配置文件编写

### MyBatis公共属性
mybatis-config.xml主要配置常用的typeAliases，设置类型别名，为Java类型设置一个短的名字。它只和XML配置有关，存在的意义仅在于用来减少类完全限定名的冗余。
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <typeAliases>
        <typeAlias alias="Integer" type="java.lang.Integer"/>
        <typeAlias alias="Long" type="java.lang.Long"/>
        <typeAlias alias="HashMap" type="java.util.HashMap"/>
        <typeAlias alias="LinkedHashMap" type="java.util.LinkedHashMap"/>
        <typeAlias alias="ArrayList" type="java.util.ArrayList"/>
        <typeAlias alias="LinkedList" type="java.util.LinkedList"/>
    </typeAliases>
</configuration>
```
这样在使用*Mapper.xml，需要引入直接这样写：
```
resultType="Integer" 
//或者
parameterType="Long"
```
### 添加映射文件
在mapper目录下，新增Mapper文件，UserMapper.xml，见src/resources/mybatis/mapper/UserMapper.xml， 在Mapper里配置表结构和类的对应关系，在编写具体的SQL

### 编写Dao层的代码
com.example.mybatis.mapper包下UserMapper.java，这里的方法名需要和 XML 配置中的 id 属性一致，不然会找不到方法去对应执行的 SQL。

### 测试代码

## 分页查询
多条件分页查询是实际工作中最常使用的功能之一，MyBatis特别擅长处理这类的问题。在实际工作中，会对分页进行简单的封装，方便前端使用。另外在Web开发规范使用中，Web层的参数会以param为后缀的对象进行传参，以result结尾的实体类封装返回的数据。

### 示例流程
1. 定义分页的基础类
    ```java
    package com.example.mybatis.param;
    
    import lombok.Data;
    
    /**
     * 分页基础类
     *
     * @author jingLv
     * @date 2020/09/27
     */
    @Data
    public class PageParam {
        /**
         * 起始行
         */
        private int beginLine;
        /**
         * 默认每页3条记录，可以根据前端传参进行修改
         */
        private Integer pageSize = 3;
        /**
         * 当前页
         */
        private Integer currentPage = 0;
    
    }
    ```
2. user的查询条件参数类继承分页基础类
    ```java
    package com.example.mybatis.param;
    
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    
    /**
     * User 的查询条件参数类继承分页基础类
     *
     * @author jingLv
     * @date 2020/09/27
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public class UserParam extends PageParam {
        /**
         * 查询条件--用户姓名
         */
        private String userName;
        /**
         * 查询条件--用户性别
         */
        private String gender;
    }
    ```
3. 配置具体的SQL，将查询条件提取出来
    ```xml
    <!--先将查询条件提取出-->
    <sql id="Base_Where_List">
        <if test="userName != null  and userName != ''">
            and user_name = #{userName}
        </if>
        <if test="gender != null and gender != ''">
            and gender = #{gender}
        </if>
    </sql>
    ```
4. 从对UserParam中获取分页信息和查询条件，进行组合
    ```xml
   <!--从对象 UserParam 中获取分页信息和查询条件，最后进行组合-->
    <select id="getList" resultMap="BaseResultMap" parameterType="com.example.mybatis.param.UserParam">
        SELECT
        <include refid="Base_Column_List"/>
        FROM users
        WHERE 1=1
        <include refid="Base_Where_List"/>
        ORDER BY id DESC
        LIMIT #{beginLine} , #{pageSize}
    </select>
   ```
5. 配置统计出查询结果的总数
    ```xml
    <!--前端需要展示总共的页码，因此需要统计出查询结果的总数-->
    <select id="getCount" resultType="Integer" parameterType="com.example.mybatis.param.UserParam">
        select
        count(1)
        from users
        where 1=1
        <include refid="Base_Where_List"/>
    </select>
    ```
6. Mapper类中定义的两个方法和配置文件相互对应
    ```java
    /**
     * 分页查询数据
     *
     * @param userParam user的分类
     * @return 返回数据集合
     */
    List<User> getList(UserParam userParam);

    /**
     * 统计出查询结果的总数
     *
     * @param userParam user的分类
     * @return 返回总数
     */
    int getCount(UserParam userParam);
    ```
7. 封装Page类返给前端
    ```java
   package com.example.mybatis.result;
   
   import com.example.mybatis.param.PageParam;
   import lombok.Data;
   
   import java.io.Serializable;
   import java.util.ArrayList;
   import java.util.List;
   
   /**
    * 分页封装数据
    * Page将分页信息和数据信息进行封装，方便前端显示第几页、总条数和数据，这样分页功能就完成了
    *
    * @author jingLv
    * @date 2020/09/27
    */
   @Data
   public class Page<E> implements Serializable {
       private static final long serialVersionUID = -2020350783443768083L;
   
       /**
        * 当前页数
        */
       private int currentPage = 1;
       /**
        * 总页数
        */
       private long totalPage;
       /**
        * 总记录数
        */
       private long totalNumber;
       /**
        * 数据集
        */
       private List<E> list;
   
       public static Page<Object> NULL = new Page<>(0, 0, 15, new ArrayList<>());
   
       public Page() {
           super();
       }
   
       /**
        * @param beginLine   当前页数
        * @param totalNumber 总记录数
        * @param pageSize    页大小
        * @param list        页数据
        */
       public Page(int beginLine, long totalNumber, int pageSize, List<E> list) {
           super();
           this.currentPage = beginLine / pageSize + 1;
           this.totalNumber = totalNumber;
           this.list = list;
           this.totalPage = totalNumber % pageSize == 0 ? totalNumber
                   / pageSize : totalNumber / pageSize + 1;
       }
   
       public Page(PageParam pageParam, long totalNumber, List<E> list) {
           super();
           this.currentPage = pageParam.getCurrentPage();
           this.totalNumber = totalNumber;
           this.list = list;
           this.totalPage = totalNumber % pageParam.getPageSize() == 0 ? totalNumber
                   / pageParam.getPageSize() : totalNumber / pageParam.getPageSize() + 1;
       }
   }
   ```
8. 测试代码
    ```java
    @Test
    public void testPage() {
        UserParam userParam = new UserParam();
        userParam.setGender("WOMAN");
        userParam.setCurrentPage(1);

        List<User> users = userMapper.getList(userParam);
        long count = userMapper.getCount(userParam);
        Page<User> page = new Page<>(userParam, count, users);
        System.out.println(page);
    }
    ```

Page 将分页信息和数据信息进行封装，方便前端显示第几页、总条数和数据，这样分页功能就完成了。

## 多数据源处理
1. 配置文件，需要配置两个不同的数据源
    ```properties
    mybatis.config-location=classpath:mybatis/mybatis-config.xml
    logging.level.com.neo.mapper=debug
    spring.datasource.one.url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
    spring.datasource.one.username=root
    spring.datasource.oen.password=123123
    spring.datasource.one.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.two.url=jdbc:mysql://localhost:3306/test_tp?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
    spring.datasource.two.username=root
    spring.datasource.two.password=123123
    spring.datasource.two.driver-class-name=com.mysql.cj.jdbc.Driver
    ```
    注意，需要提前在 test1 和 test2 库中创建好 User 表结构。
    
    - 第一个数据源以 spring.datasource.one.* 为前缀连接数据库 test
    - 第二个数据源以 spring.datasource.two.* 为前缀连接数据库 test_tp
2. 在resources/mybatis/mapper下，分别创建两个目录，创建各自的*Mapper.xml文件，对应的mapper包下分别创建两个包，创建个子的*Mapper.java
2. 数据源配置类，创建config包，分别创建DataSource1Config.java和DataSource2Config.java
    - 从上面的步骤我们可以总结出来，创建多数据源的过程就是：首先创建 DataSource，注入到 SqlSessionFactory 中，再创建事务，将 SqlSessionFactory 注入到创建的 SqlSessionTemplate 中，最后将 SqlSessionTemplate 注入到对应的 Mapper 包路径下。其中需要指定分库的 Mapper 包路径。
    - 注意，在多数据源的情况下，我们不需要在启动类添加：@MapperScan("com.xxx.mapper") 的注解。
3. 测试，配置好多数据源之后，在项目中想使用哪个数据源就把对应数据源注入到类中使用即可