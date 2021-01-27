# SpringBoot EasyPOI实现Excel导入导出

## 环境搭建

### 1. 引入依赖
- mybatis
- mysql
- druid
- easyPOI
- thymeleaf
- lombok

```xml
        <!--MyBatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.4</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--Druid-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.2.2</version>
        </dependency>

        <!--thymeleaf-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <!--EasyPOI-->
        <dependency>
            <groupId>cn.afterturn</groupId>
            <artifactId>easypoi-base</artifactId>
            <version>${easypoi.version}</version>
        </dependency>

        <dependency>
            <groupId>cn.afterturn</groupId>
            <artifactId>easypoi-web</artifactId>
            <version>${easypoi.version}</version>
        </dependency>

        <dependency>
            <groupId>cn.afterturn</groupId>
            <artifactId>easypoi-annotation</artifactId>
            <version>${easypoi.version}</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.16</version>
        </dependency>
```

### 2. 编写配置文件
```properties
server.port=8087
spring.application.name=easypoi
# 数据源
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123123
# mybatis
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.example.excel.demo.entity
# thymeleaf缓存
spring.thymeleaf.cache=false
```

### 3. 创建包结构

![image-20210126111817114](https://gitee.com/JeanLv/study_image/raw/master///image-20210126111817114.png)

### 4. 启动项目查看是否有问题

![image-20210126112215079](https://gitee.com/JeanLv/study_image/raw/master///image-20210126112215079.png)



## 开发测试页面

在resources/templates目录下创建index.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>导入excel的主页面</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <h1>选择Excel文件导入到数据中</h1>
            <form action="" method="post" class="form-inline">
                <div class="form-group">
                    <input class="form-control" type="file" name="excelFile">
                    <input type="submit" class="btn btn-danger" value="导入数据">
                </div>
            </form>
        </div>
        <div class="col-md-12">
            <h1>显示导入数据列表</h1>
            <table class="table table-bordered">
                <tr>
                    <th>编号</th>
                    <th>头像</th>
                    <th>姓名</th>
                    <th>年龄</th>
                    <th>生日</th>
                </tr>
                <tr>
                    <td>1</td>
                    <td><img src="" alt=""></td>
                    <td>阿花</td>
                    <td>23</td>
                    <td>2012-12-12</td>
                </tr>
                <tr>
                    <td>1</td>
                    <td><img src="" alt=""></td>
                    <td>阿花</td>
                    <td>23</td>
                    <td>2012-12-12</td>
                </tr>
            </table>

            <hr>
            <input type="button" class="btn btn-info" value="导出excel">
        </div>
    </div>
</div>
</body>
</html>
```

静态页面

![image-20210126112441259](https://gitee.com/JeanLv/study_image/raw/master///image-20210126112441259.png)

新增控制器，IndexController.java

```java
package com.example.excel.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String index() {
        return "index";
    }
}

```

启动项目，访问地址：http://localhost:8087/index

![image-20210126112732927](https://gitee.com/JeanLv/study_image/raw/master///image-20210126112732927.png)



## 数据建模

### 1. 准备数据Excel

![image-20210126113342614](https://gitee.com/JeanLv/study_image/raw/master///image-20210126113342614.png)

### 2. 根据Excel抽取表结构

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `bir` timestamp NULL DEFAULT NULL,
  `age` int(4) DEFAULT NULL,
  `photo` varchar(150) DEFAULT NULL,
  `status` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
```



### 3. 创建实体类

```java
package com.example.excel.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Data
@ExcelTarget("users")
public class User implements Serializable {

    private static final long serialVersionUID = -9205298549640800970L;

    @Excel(name = "编号")
    private String id;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "生日", format = "yyyy年MM月dd日")
    private Date bir;
    @Excel(name = "头像", type = 2, savePath = "/Users/apple/JavaProject/spring-boot-example/spring-boot-easypoi/src/main/resources/static/imgs/")
    private String photo;
    @Excel(name = "年龄", width = 12.0)
    private Integer age;
    @Excel(name = "状态", replace = {"激活_1", "锁定_0"})
    private String status;
}


```



### 4. 创建DAO接口

UserDAO.java

```java
package com.example.excel.demo.dao;

import com.example.excel.demo.entity.User;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
public interface UserDAO {

    /**
     * 查询所有用户信息
     *
     * @return 返回用户信息列表
     */
    List<User> findAll();
}
```

UserMapper.xml

```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.excel.demo.dao.UserDAO">
    <!--查询所有-->
    <select id="findAll" resultType="com.example.excel.demo.entity.User">
        SELECT `id`, `name`, `bir`, `age`, `photo`, `status`
        FROM `t_user`
    </select>
</mapper>
```



### 5. 创建Service接口&实现

UserService.java

```java
package com.example.excel.demo.service;

import com.example.excel.demo.entity.User;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
public interface UserService {

    /**
     * 查询所有用户信息
     *
     * @return 返回用户信息列表
     */
    List<User> findAll();
}

```



UserServiceImpl.java

```java
package com.example.excel.demo.service.impl;

import com.example.excel.demo.dao.UserDAO;
import com.example.excel.demo.entity.User;
import com.example.excel.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * 查询所有用户信息
     *
     * @return 返回用户信息列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> findAll() {
        return userDAO.findAll();
    }
}
```



### 6. 创建Controller完成查询所有

UserController.java

```java
package com.example.excel.demo.controller;

import com.example.excel.demo.entity.User;
import com.example.excel.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有
     *
     * @param request HttpServletRequest
     * @return 返回页面
     */
    @RequestMapping("/list")
    public String findAll(HttpServletRequest request) {
        List<User> users = userService.findAll();
        request.setAttribute("users", users);
        return "index";
    }
}
```



### 7. 修改index.html展示所有数据

注意：`引入thymeleaf语法解析:<html lang="en" xmlns:th="http://www.thymeleaf.org">`

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>导入excel的主页面</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <h1>选择Excel文件导入到数据中</h1>
            <form th:action="@{/user/import}" method="post" enctype="multipart/form-data" class="form-inline">
                <div class="form-group">
                    <input class="form-control" type="file" name="excelFile">
                    <input type="submit" class="btn btn-danger" value="导入数据">
                </div>
            </form>
        </div>
        <div class="col-md-12">
            <h1>显示导入数据列表</h1>
            <table class="table table-bordered">
                <tr>
                    <th>编号</th>
                    <th>头像</th>
                    <th>姓名</th>
                    <th>年龄</th>
                    <th>生日</th>
                    <th>状态</th>
                </tr>
                <tr th:each="user : ${users}">
                    <td th:text="${user.id}"></td>
                    <td><img th:src="${'/imgs/'+ user.photo}" height="60px" alt=""></td>
                    <td th:text="${user.name}"></td>
                    <td th:text="${#dates.format(user.bir,'yyyy-MM-dd')}"></td>
                    <td th:text="${user.age}"></td>
                    <td th:text="${user.status}"></td>
                </tr>
            </table>
            <hr>
            <a th:href="@{/user/export}" class="btn btn-info">导出excel</a>
        </div>
    </div>
</div>

</body>
</html>
```



### 8. 启动项目进行访问

项目启动后，访问地址：http://localhost:8087/user/list

![image-20210126115541879](https://gitee.com/JeanLv/study_image/raw/master///image-20210126115541879.png)

由于数据库，目前没有数据，页面显示则是没有数据的



## 导入数据

1. 开发DAO&Mapper

   UserDAO.java中新增save方法

   ```java
   package com.example.excel.demo.dao;
   
   import com.example.excel.demo.entity.User;
   
   import java.util.List;
   
   /**
    * @author jingLv
    * @date 2021/01/26
    */
   public interface UserDAO {
   
       /**
        * 查询所有用户信息
        *
        * @return 返回用户信息列表
        */
       List<User> findAll();
   
       /**
        * 保存所有用户信息
        *
        * @param user 用户信息
        * @return 返回结果
        */
       int save(User user);
   }
   
   ```

   UserMapper.xml中新增插入SQL语句

   ```xml
   <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.example.excel.demo.dao.UserDAO">
   
       <!--查询所有-->
       <select id="findAll" resultType="com.example.excel.demo.entity.User">
           SELECT `id`, `name`, `bir`, `age`, `photo`, `status`
           FROM `t_user`
       </select>
   
       <!--插入用户信息-->
       <insert id="save" parameterType="com.example.excel.demo.entity.User" useGeneratedKeys="true" keyProperty="id">
           insert into `t_user`
           values (#{id}, #{name}, #{bir}, #{age}, #{photo}, #{status})
       </insert>
   </mapper>
   ```

   

2. 开发Service接口和实现类

   UserService.java新增保存所有用户信息接口

   ```java
   package com.example.excel.demo.service;
   
   import com.example.excel.demo.entity.User;
   
   import java.util.List;
   
   /**
    * @author jingLv
    * @date 2021/01/26
    */
   public interface UserService {
   
       /**
        * 查询所有用户信息
        *
        * @return 返回用户信息列表
        */
       List<User> findAll();
   
       /**
        * 保存所有用户信息
        *
        * @param users 用户信息
        */
       void saveAll(List<User> users);
   }
   
   ```

   UserServiceImpl.java新增保存所有用户信息的实现

   ```java
   package com.example.excel.demo.service.impl;
   
   import com.example.excel.demo.dao.UserDAO;
   import com.example.excel.demo.entity.User;
   import com.example.excel.demo.service.UserService;
   import org.springframework.stereotype.Service;
   import org.springframework.transaction.annotation.Propagation;
   import org.springframework.transaction.annotation.Transactional;
   
   import java.util.List;
   
   /**
    * @author jingLv
    * @date 2021/01/26
    */
   @Service
   public class UserServiceImpl implements UserService {
   
       private final UserDAO userDAO;
   
       public UserServiceImpl(UserDAO userDAO) {
           this.userDAO = userDAO;
       }
   
       /**
        * 查询所有用户信息
        *
        * @return 返回用户信息列表
        */
       @Override
       @Transactional(propagation = Propagation.SUPPORTS)
       public List<User> findAll() {
           return userDAO.findAll();
       }
   
       /**
        * 保存所有用户信息
        *
        * @param users 用户信息
        */
       @Override
       public void saveAll(List<User> users) {
           users.forEach(user -> {
               user.setId(null);
               String photoFileName = user.getPhoto().substring(user.getPhoto().lastIndexOf("/") + 1);
               user.setPhoto(photoFileName);
               userDAO.save(user);
           });
       }
   }
   ```

   

3. 开发controller

   ```java
   package com.example.excel.demo.controller;
   
   import cn.afterturn.easypoi.excel.ExcelImportUtil;
   import cn.afterturn.easypoi.excel.entity.ImportParams;
   import com.example.excel.demo.entity.User;
   import com.example.excel.demo.service.UserService;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.stereotype.Controller;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.multipart.MultipartFile;
   
   import javax.servlet.http.HttpServletRequest;
   import java.util.List;
   
   /**
    * @author jingLv
    * @date 2021/01/26
    */
   @Slf4j
   @Controller
   @RequestMapping("/user")
   public class UserController {
   
       private final UserService userService;
   
       public UserController(UserService userService) {
           this.userService = userService;
       }
   
       /**
        * 查询所有
        *
        * @param request HttpServletRequest
        * @return 返回页面
        */
       @RequestMapping("list")
       public String findAll(HttpServletRequest request) {
           List<User> users = userService.findAll();
           request.setAttribute("users", users);
           return "index";
       }
   
       /**
        * 导入Excel文件
        *
        * @param excelFile excel文件
        * @return 返回跳转list页面
        */
       @RequestMapping("import")
       public String importExcel(MultipartFile excelFile) throws Exception {
           log.info("文件名：{}", excelFile.getOriginalFilename());
           // excel导入
           ImportParams params = new ImportParams();
           List<User> users = ExcelImportUtil.importExcel(excelFile.getInputStream(), User.class, params);
           userService.saveAll(users);
           // 上传完成之后跳转到查询所有信息路径
           return "redirect:/user/list";
       }
   }
   
   ```

4. 修改配置文件

   application.properties中配置图片的静态路径

   ```properties
   # 定义存储图片路径
   upload.dir=/Users/apple/JavaProject/spring-boot-example/spring-boot-easypoi/src/main/resources/static/imgs
   # 静态路径放行
   spring.web.resources.static-locations=classpath:/static,file:${upload.dir}
   ```

5. 编辑页面index.html

   - 表单提交方式必须是post
   - 表单的enctype必须为multipart/form-data

   ```html
   <!DOCTYPE html>
   <html lang="en" xmlns:th="http://www.thymeleaf.org">
   <head>
       <meta charset="UTF-8">
       <title>导入excel的主页面</title>
       <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
             integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
   </head>
   <body>
   <div class="container-fluid">
       <div class="row">
           <div class="col-md-12">
               <h1>选择Excel文件导入到数据中</h1>
               <form th:action="@{/user/import}" method="post" enctype="multipart/form-data" class="form-inline">
                   <div class="form-group">
                       <input class="form-control" type="file" name="excelFile">
                       <input type="submit" class="btn btn-danger" value="导入数据">
                   </div>
               </form>
           </div>
           <div class="col-md-12">
               <h1>显示导入数据列表</h1>
               <table class="table table-bordered">
                   <tr>
                       <th>编号</th>
                       <th>头像</th>
                       <th>姓名</th>
                       <th>生日</th>
                       <th>年龄</th>
                       <th>状态</th>
                   </tr>
                   <tr th:each="user : ${users}">
                       <td th:text="${user.id}"></td>
                       <td><img th:src="${'/imgs/'+ user.photo}" height="60px" alt=""></td>
                       <td th:text="${user.name}"></td>
                       <td th:text="${#dates.format(user.bir,'yyyy-MM-dd')}"></td>
                       <td th:text="${user.age}"></td>
                       <td th:text="${user.status}"></td>
                   </tr>
               </table>
               <hr>
               <a th:href="@{/user/export}" class="btn btn-info">导出excel</a>
           </div>
       </div>
   </div>
   
   </body>
   </html>
   ```

   

6. 启动项目导入Excel数据及查看结果

   ![image-20210127114556805](https://gitee.com/JeanLv/study_image/raw/master///image-20210127114556805.png)

## 导出数据

1. 开发Controller，下载功能

   ```java
       /**
        * 导出excel
        *
        * @param response 响应response
        * @throws IOException io异常
        */
       @RequestMapping("export")
       public void exportExcel(HttpServletResponse response) throws IOException {
           // 查询数据库User的所有数据
           List<User> users = userService.findAll();
           // 用户图片的处理
           users.forEach(user -> {
               try {
                   Excel excelAnn = user.getClass().getDeclaredField("photo").getAnnotation(Excel.class);
                   user.setPhoto(excelAnn.savePath() + '/' + user.getPhoto());
               } catch (NoSuchFieldException e) {
                   e.printStackTrace();
               }
           });
           // 设置response响应头信息
           response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode("用户列表.xls", "UTF-8"));
           // 生成Excel
           Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户列表信息", "用户信息"), User.class, users);
           ServletOutputStream outputStream = response.getOutputStream();
           workbook.write(outputStream);
           outputStream.close();
           workbook.close();
       }
   ```

   

2. 编辑index.html

   ```html
   <!DOCTYPE html>
   <html lang="en" xmlns:th="http://www.thymeleaf.org">
   <head>
       <meta charset="UTF-8">
       <title>导入excel的主页面</title>
       <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
             integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
   </head>
   <body>
   <div class="container-fluid">
       <div class="row">
           <div class="col-md-12">
   							……
           </div>
           <div class="col-md-12">
          				……
               <hr>
               <a th:href="@{/user/export}" class="btn btn-info">导出excel</a>
           </div>
       </div>
   </div>
   
   </body>
   </html>
   ```

   

3. 启动项目测试excel导出及结果

   ![image-20210127115731093](https://gitee.com/JeanLv/study_image/raw/master///image-20210127115731093.png)

