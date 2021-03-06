# 权限的管理
## 什么是权限管理
基本上涉及到用户参与的系统都要进行权限管理，权限管理属于系统安全的范畴，权限管理实现对用户访问系统的控制，按照安全规则或者**安全策略**控制用户可以访问而且只能访问自己被授权的资源。

权限管理包括用户**身份认证**和**授权**两部分，简称**认证授权**。对于需要访问控制的资源用户首先经过身份认证，认证通过后用户具有该资源的访问权限，方可访问。

## 什么是身份认证
**身份认证**，就是判断一个用户是否为合法用户的处理过程。最常用的简单身份认证方式是系统通过核对用户输入的用户名和口令，看其是否与系统中存储的该用户的用户名和口令一致，来判断用户身份是否正确。对于采用指纹等系统，则出示指纹；对于硬件key等刷卡系统，则需要刷卡。

## 什么是授权
**授权，即访问控制**，控制谁能访问哪些资源。主体进行身份认证后需要分配权限方可系统的资源，对于某些资源没有权限是无法访问的。

# Shiro
## 什么是Shiro
[Shiro的官方网站](http://shiro.apache.org)

Shiro是一个功能强大且易用使用的Java安全框架，它执行身份验证、授权、加密和会话管理。使用Shiro易于理解的API，您可以快速轻松地保护任何应用程序——从最小的移动应用程序到最大的Web和企业应用程序。

Shiro是Apache旗下一个开源框架，它将软件系统的安全认证相关的功能抽取出来，实现用户身份认证、权限授权、加密、会话管理等功能，组成一个通用的安全认证框架。

## Shiro的核心框架

![img](https://gitee.com/JeanLv/study_image2/raw/master///3-20200920145523699.png)

### 1. Subject主体

外部应用与subject进行交互，subject记录了当前操作用户，将用户的概念理解为当前操作的主体，可能是一个通过浏览器请求的用户，也可能是一个运行的程序。	

Subject在shiro中是一个接口，接口中定义了很多认证授相关的方法，外部程序通过subject进行认证授，而subject是通过SecurityManager安全管理器进行认证授权

### 2. SecurityManager安全管理器

对全部的subject进行安全管理，它是shiro的核心，负责对所有的subject进行安全管理。通过SecurityManager可以完成subject的认证、授权等，实质上SecurityManager是通过Authenticator进行认证，通过Authorizer进行授权，通过SessionManager进行会话管理等。

`SecurityManager是一个接口，继承了Authenticator, Authorizer, SessionManager这三个接口。`

### 3. Authenticator认证器

对用户身份进行认证，Authenticator是一个接口，shiro提供ModularRealmAuthenticator实现类，通过ModularRealmAuthenticator基本上可以满足大多数需求，也可以自定义认证器。

### 4. Authrizer授权器

用户通过认证器认证通过，在访问功能时需要通过授权器判断用户是否有此功能的操作权限。

### 5. Realm领域

相当于datasource数据源，securityManager进行安全认证需要通过Realm获取用户权限数据，比如：如果用户身份数据在数据库那么realm就需要从数据库获取用户身份信息。

- ​	注意：不要把realm理解成只是从数据源取数据，在realm中还有认证授权校验的相关的代码。

### 6.  SessionManager会话管理

shiro框架定义了一套会话管理，它不依赖web容器的session，所以shiro可以使用在非web应用上，也可以将分布式应用的会话集中在一点管理，此特性可使它实现单点登录。

### 7. SessionDAO会话dao

是对session会话操作的一套接口，比如要将session存储到数据库，可以通过jdbc将会话存储到数据库。

### 8. CacheManager缓存管理

将用户权限数据存储在缓存，这样可以提高性能。

### 9. Cryptography密码管理

shiro提供了一套加密/解密的组件，方便开发。比如提供常用的散列、加/解密等功能。



## Shiro中的认证

### 认证

身份认证，就是判断一个用户是否为合法用户的处理过程。最常用的简单身份认证方式是系统通过核对用户输入的用户名和口令，看其是否与系统中存储的该用户的用户名和口令一致，来判断用户身份是否正确。

### Shiro中认证的关键对象

- Subject：主体

  访问系统的用户，主体可以使用户、程序等，进行认证的都称为主体。

- Principal：身份信息

  是主体（subject）进行身份认证的标识，标识必须具有**唯一性**，如用户名、手机号、邮箱地址等，一个主体可以有多个身份，但是必须有一个主身份（Primary|Principle）

- credential：凭证信息

  是只有主体自己知道的安全信息，如密码、证书等。

### 认证的流程

![image-20200920152511019](https://gitee.com/JeanLv/study_image2/raw/master///image-20200920152511019.png)

### 认证的开发

1. 创建maven项目并引入依赖

   ```xml
   <dependency>
     <groupId>org.apache.shiro</groupId>
     <artifactId>shiro-core</artifactId>
     <version>1.3.2</version>
   </dependency>
   ```

   

2. 引入配置文件

   Shiro的配置文件是.ini结尾文件，.ini可以写复杂数据格式 -- 整合Springboot是用不到的，这个是用来在学习Shiro书写系统中相关权限数据

   ```ini
   [users]
   admin=admin
   jack=123456
   ```

   ![image-20210121164640609](https://gitee.com/JeanLv/study_image/raw/master///image-20210121164640609.png)

3. 认证开发代码

   ```java
   package com.demo.shiro;
   
   import org.apache.shiro.SecurityUtils;
   import org.apache.shiro.authc.IncorrectCredentialsException;
   import org.apache.shiro.authc.UnknownAccountException;
   import org.apache.shiro.authc.UsernamePasswordToken;
   import org.apache.shiro.mgt.DefaultSecurityManager;
   import org.apache.shiro.realm.text.IniRealm;
   import org.apache.shiro.subject.Subject;
   
   /**
    * @author jinglv
    * @date 2020/09/20
    */
   public class TestAuthenticator {
   
       public static void main(String[] args) {
           // 1.创建安全管理器对象
           DefaultSecurityManager securityManager = new DefaultSecurityManager();
   
           // 2.给安全管理器设置Realm
           securityManager.setRealm(new IniRealm("classpath:shiro.ini"));
   
           // 3.SecurityUtils 给全局安全工具类设置安全管理器
           SecurityUtils.setSecurityManager(securityManager);
   
           // 4.关键对象：Subject主体
           Subject subject = SecurityUtils.getSubject();
   
           // 5.创建令牌
           UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
   
           try {
               // 6.用户认证
               System.out.println("认证前状态：" + subject.isAuthenticated());
               subject.login(token);
               System.out.println("认证后状态：" + subject.isAuthenticated());
           } catch (UnknownAccountException e) {
               e.printStackTrace();
               System.out.println("认证失败：用户名不存在");
           } catch (IncorrectCredentialsException e) {
               e.printStackTrace();
               System.out.println("认证失败：密码错误");
           }
   
       }
   }
   ```

   提示：一些常用的异常

   - DisabledAccountException（帐号被禁用）
   - LockedAccountException（帐号被锁定）
   - ExcessiveAttemptsException（登录失败次数过多）
   - ExpiredCredentialsException（凭证过期

## 自定义Realm

上边的程序使用的是Shiro自带的IniRealm，IniRealm从ini配置文件中读取用户的信息，大部分情况下需要从系统的数据库中读取用户信息，所以需要自定义realm。

### shiro提供的Realm

![image-20200921184612980](https://gitee.com/JeanLv/study_image/raw/master///image-20200921184612980.png)

### 根据认证源码认证使用的是SimpleAccountRealm

![image-20200921184708924](https://gitee.com/JeanLv/study_image/raw/master///image-20200921184708924.png)

SimpleAccountRealm的部分源码中有两个方法一个是**认证**一个是**授权**

AuthorizingRealm 授权Realm doGetAuthorizationInfo

AuthenticatingRealm 认证Realm doGetAuthenticationInfo

### 自定义Realm

```java
package com.demo.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义Realm实现，将认证/授权数据的来源转为数据库
 *
 * @author jingLv
 * @date 2020/09/22
 */
public class CustomerRealm extends AuthorizingRealm {
    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 在token中获取用户名
        String principal = (String) authenticationToken.getPrincipal();
        // 根据身份信息使用jdbc mybatis查询相关数据库
        if ("admin".equals(principal)) {
            // 参数1：返回数据库中的正确的用户名
            // 参数2：返回数据库中的正确的密码
            // 参数3：提供当前realm的名字，使用this.getName()
            return new SimpleAuthenticationInfo(principal, "admin", this.getName());
        }
        return null;
    }
}

```



### 使用自定义Realm认证

```java
package com.demo.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * 使用自定义realm
 *
 * @author jingLv
 * @date 2020/09/22
 */
public class TestAuthenticatorCustomerRealm {

    public static void main(String[] args) {
        // 创建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //设置自定义realm
        defaultSecurityManager.setRealm(new CustomerRealm());
        //给安全工具类设置安全管理器
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //通过安全工具类获取subject
        Subject subject = SecurityUtils.getSubject();
        // 创建token
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin");

        try {
            subject.login(token);
            System.out.println("登录成功--");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误！");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误！");
        }
    }
}
```



## 使用MD5和Salt

实际应用是将盐和散列后的值存在数据库中，自动realm从数据库取出盐和加密后的值由shiro完成密码校验。



MD5算法：

- 作用：一般用来加密或者签名（校验和）
- 特点：
  - MD5算法不可逆
  - 如果内容相同，无论执行多少次MD5生成的结果始终是一致的
  - 生成结果：始终是一个16进制32位长度字符串



盐（Salt），在密码学中，是指通过在密码任意固定位置插入特定的字符串，让散列后的结果和使用原始密码的散列结果不相符，这种过程称之为“加盐”。



### 测试Shiro中加密的字符串

```java
package com.demo.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author jingLv
 * @date 2020/09/23
 */
public class TestShiroMD5 {
    public static void main(String[] args) {
        //创建一个MD5算法
/*        Md5Hash md5Hash = new Md5Hash();

        md5Hash.setBytes("123".getBytes());

        String s = md5Hash.toHex();
        System.out.println(s);*/

        // 使用MD5
        Md5Hash md5Hash = new Md5Hash("123");
        System.out.println(md5Hash.toHex());

        // 使用MD5 + Salt处理
        Md5Hash md5Hash1 = new Md5Hash("123", "X0*7ps");
        System.out.println(md5Hash1.toHex());

        // 使用MD5 + salt + hash散列
        Md5Hash md5Hash2 = new Md5Hash("123", "X0*7ps", 1024);
        System.out.println(md5Hash2.toHex());
    }
}
```

### 自定义md5+salt/+(1024散列)的realm

```java
package com.demo.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 使用自定义realm加入md5+salt+hash
 *
 * @author jingLv
 * @date 2020/09/23
 */
public class CustomerMD5Realm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取身份信息
        String principal = (String) authenticationToken.getPrincipal();
        // 根据用户名查询数据库
        if ("admin".equals(principal)) {
            // 数据库查到的密码是个密文
            //参数1：数据库用户名
            //参数2：数据库md5+salt/1024散列之后的密码
            //参数3：注册时的随机盐
            //参数4：realm的名字
            return new SimpleAuthenticationInfo(principal, "e4f9bf3e0c58f045e62c23c533fcf633", ByteSource.Util.bytes("X0*7ps"), this.getName());
        }
        return null;
    }
}
```

### 使用md5+salt/+(1024散列)的认证

```java
package com.demo.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * @author jingLv
 * @date 2020/09/23
 */
public class TestAuthenticatorCustomerMD5Realm {
    public static void main(String[] args) {
        // 创建安全管理器
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 设置自定义realm获取认证数据
        CustomerMD5Realm md5Realm = new CustomerMD5Realm();
        // 设置realm使用hash凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        // 设置散列次数
        credentialsMatcher.setHashIterations(1024);
        md5Realm.setCredentialsMatcher(credentialsMatcher);
        //注入realm
        defaultSecurityManager.setRealm(md5Realm);
        // 将安全管理器注入安全工具
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        // 通过安全工具获取subject
        Subject subject = SecurityUtils.getSubject();
        // 认证
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "123");

        try {
            subject.login(token);
            System.out.println("登录成功--");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误！");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误！");
        }
    }
}
```



## Shiro中的授权

### 授权

授权，即访问控制，控制谁能访问哪些资源。主体进行身份认证后需要分配权限可访问系统的资源，对于某些资源没有权限是无法访问的。

### 关键对象

授权可简单理解为who对what(which)进行How操作：

- Who:即主体（Subject），主体需要访问系统中的资源。
- What:即资源（Resource），如系统菜单、页面、按钮、类方法，系统商品信息等。资源包含资源类型和资源实例，比如商品信息为资源类型，类型为t01的商品为资源实例，编号为001的商品信息也属于资源实例。
- How:权限/许可（Permission），规定了主体对资源的操作许可，权限离开资源没有意义，如用户查询权限，用户添加权限，某个类方法的调用权限，编号为001用户的修改权限等，通过权限可知主体对哪些资源都有哪些操作许可。

### 授权流程

![image-20200927113729438](https://gitee.com/JeanLv/study_image/raw/master///image-20200927113729438.png)

### 授权方式

- 基于**角色**的访问控制

  - RBAC基于角色的访问控制（Role-Based Access Control）是以角色为中心进行访问控制

    ```java
    if(subject.hashRole("admin")) {
      // 操作什么资源
    }
    ```

    

- 基于**资源**的访问控制

  - RBAC基于资源的访问控制（Resource-Based Access Control）是以资源为中心进行访问控制

    ```java
    if(subject.isPermission("user:update:01")) { // 资源实例
      // 对01的用户具有更新权限
    }
    
    if(subject.isPermission("user:update:*")) { // 资源类型
      // 对所有的用户具有更新权限
    }
    ```



### 权限字符串

权限字符串的规则是：**资源标识符:操作:资源实例标识符**，意识是对哪个资源的哪个实例具有什么操作，":"是资源/操作/实例的分割符，权限字符串也可以使用`*`通配符

例子：

- 用户创建权限：`user:create`，或`user:create:*`
- 用户修改实例001的权限：`user:update:001`
- 用户实例001的所有权限：`user:*：001`



### Shiro中授权编程实现方式

- 编程式

  ```java
  Subject subject = SecurityUtils.getSubject();
  if(subject.hasRole(“admin”)) {
  	//有权限
  } else {
  	//无权限
  }
  ```

  

- 注解式

  ```java
  @RequiresRoles("admin")
  public void hello() {
  	//有权限
  }
  ```

  

- 标签式

  ```jsp
  JSP/GSP 标签：在JSP/GSP 页面通过相应的标签完成：
  <shiro:hasRole name="admin">
  	<!— 有权限—>
  </shiro:hasRole>
  注意: Thymeleaf 中使用shiro需要额外集成!
  ```



### 授权的开发

#### 自定义Realm中的实现

```java
package com.demo.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义Realm实现，将认证/授权数据的来源转为数据库
 *
 * @author jingLv
 * @date 2020/09/27
 */
public class CustomerRealm extends AuthorizingRealm {
    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("身份信息：" + primaryPrincipal);

        //根据身份信息 用户名 获取当前用户的角色信息，以及权限信息 admin user super
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 将数据库中查询角色信息赋值给权限对象
        simpleAuthorizationInfo.addRole("admin");
        simpleAuthorizationInfo.addRole("user");

        // 将数据库中查询权限信息赋值给权限对象
        simpleAuthorizationInfo.addStringPermission("user:*:01");
        simpleAuthorizationInfo.addStringPermission("product:create");

        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 在token中获取用户名
        String principal = (String) authenticationToken.getPrincipal();
        // 根据身份信息使用jdbc mybatis查询相关数据库
        if ("admin".equals(principal)) {
            // 参数1：返回数据库中的正确的用户名
            // 参数2：返回数据库中的正确的密码
            // 参数3：提供当前realm的名字，使用this.getName()
            return new SimpleAuthenticationInfo(principal, "admin", this.getName());
        }
        return null;
    }
}
```



#### 授权

```java
package com.demo.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

/**
 * 使用自定义realm
 *
 * @author jingLv
 * @date 2020/09/27
 */
public class TestAuthenticatorCustomerRealm {
    public static void main(String[] args) {
        // 创建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //设置自定义realm
        defaultSecurityManager.setRealm(new CustomerRealm());
        //给安全工具类设置安全管理器
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //通过安全工具类获取subject
        Subject subject = SecurityUtils.getSubject();
        // 创建token
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin");

        try {
            subject.login(token);
            System.out.println("登录成功--");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误！");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误！");
        }


        // 认证用户进行判断
        if (subject.isAuthenticated()) {
            // 基于角色的权限控制
            boolean admin = subject.hasRole("admin");
            System.out.println(admin);

            // 基于多权限的角色控制
            boolean all = subject.hasAllRoles(Arrays.asList("admin", "user"));
            System.out.println(all);

            // 是否具有其中一个角色
            boolean[] booleans = subject.hasRoles(Arrays.asList("amdin", "super", "user"));
            for (boolean aBoolean : booleans) {
                System.out.println(aBoolean);
            }

            // 基于权限字符串的访问控制，资源标识符:操作:资源
            System.out.println("权限：" + subject.isPermitted("user:*:01"));
            System.out.println("权限：" + subject.isPermitted("product:create"));

            // 分别具有哪些权限
            boolean[] booleans1 = subject.isPermitted("user:*:01", "order:*:01");
            for (boolean b : booleans1) {
                System.out.println(b);
            }

            // 同时具有哪些权限
            boolean permittedAll = subject.isPermittedAll("user:*:01", "product:create:01");
            System.out.println(permittedAll);
        }
    }
}
```

# Shiro整合SpringBoot项目

## 整合思路

![image-20201001111006232](https://gitee.com/JeanLv/study_image2/raw/master///image-20201001111006232.png)

## 环境搭建

本次实例是springboot+shiro+jsp搭建的环境

1. 创建SpringBoot并引入依赖

   ```xml
   <!--Shiro整合SpringBoot依赖-->
   <dependency>
     <groupId>org.apache.shiro</groupId>
     <artifactId>shiro-spring-boot-starter</artifactId>
     <version>1.6.0</version>
   </dependency>
   
   <!--jsp解析依赖-->
   <dependency>
     <groupId>org.apache.tomcat.embed</groupId>
     <artifactId>tomcat-embed-jasper</artifactId>
     <version>8.5.31</version>
   </dependency>
   
   <dependency>
     <groupId>jstl</groupId>
     <artifactId>jstl</artifactId>
     <version>1.2</version>
   </dependency>
   ```

   

2. 配置jsp

   - 在src/main创建一个webapp目录，并在该目录下创建jsp文件：index.jsp

     ![image-20201003154016946](https://gitee.com/JeanLv/study_image2/raw/master///image-20201003154016946.png)

   - 在配置文件中进行配置，application.properties

     ```properties
     server.port=8088
     server.servlet.context-path=/shiro
     spring.application.name=shiro
     # jsp配置
     spring.mvc.view.prefix=/
     spring.mvc.view.suffix=.jsp
     ```

   - 完成后，启动项目，访问地址：http://localhost:8088/shiro/index.jsp

     - 这是会遇到这个问题：访问404，找不到jsp，这是找不到对应的工作目录，解决方式如下

       ![image-20201003154327528](https://gitee.com/JeanLv/study_image2/raw/master///image-20201003154327528.png)

       ![image-20201003154423486](https://gitee.com/JeanLv/study_image2/raw/master///image-20201003154423486.png)

       ![image-20201003154511504](https://gitee.com/JeanLv/study_image2/raw/master///image-20201003154511504.png)

       点击[OK]后，mvn clean install，重新启动项目即可

3. 配置Shiro

   - 创建配置类，创建config包，在该包下创建ShiroConfig.java，在该配置类配置如下内容

     - 配置ShiroFilterFactoryBean，并配置认证和授权规则

     - 配置WebSecurityManager

     - 配置自定义realm

       ```java
       package com.example.shiro.config;
       
       import com.example.shiro.realm.UserRealm;
       import org.apache.shiro.realm.Realm;
       import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
       import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
       import org.springframework.context.annotation.Bean;
       import org.springframework.context.annotation.Configuration;
       
       import java.util.HashMap;
       import java.util.Map;
       
       /**
        * 用来整合Shiro框架相关的配置类
        *
        * @author jinglv
        * @date 2020/10/03
        */
       @Configuration
       public class ShiroConfig {
       
           /**
            * 1. 创建shiroFilter
            * 负责拦截所有请求
            *
            * @return
            */
           @Bean
           public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
               ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
               // 给filter设置安全管理器
               shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
       
               // 配置系统受限资源
               // 配置系统公共资源
               Map<String, String> map = new HashMap<>();
               // autch请求这个资源需要认证和授权
               map.put("/index.jsp", "authc");
       
               // 默认认证界面路径，不写的话，默认为login
               shiroFilterFactoryBean.setLoginUrl("/login.jsp");
               shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
               return shiroFilterFactoryBean;
           }
       
           /**
            * 2. 创建安全管理器
            *
            * @return
            */
           @Bean
           public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm) {
               DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
               // 给安全管理器设置Realm
               defaultWebSecurityManager.setRealm(realm);
               return defaultWebSecurityManager;
           }
       
           /**
            * 3. 创建自定义realm
            *
            * @return
            */
           @Bean
           public Realm getRealm() {
               UserRealm userRealm = new UserRealm();
               return userRealm;
           }
       }
       ```

       

   - 创建自定义Realm，暂时是空的

     ```java
     package com.example.shiro.realm;
     
     import org.apache.shiro.authc.AuthenticationException;
     import org.apache.shiro.authc.AuthenticationInfo;
     import org.apache.shiro.authc.AuthenticationToken;
     import org.apache.shiro.authz.AuthorizationInfo;
     import org.apache.shiro.realm.AuthorizingRealm;
     import org.apache.shiro.subject.PrincipalCollection;
     
     /**
      * 自定义Realm
      *
      * @author jinglv
      * @date 2020/10/03
      */
     public class UserRealm extends AuthorizingRealm {
         /**
          * 处理授权
          *
          * @param principalCollection
          * @return
          */
         @Override
         protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
             return null;
         }
     
         /**
          * 处理认证
          *
          * @param authenticationToken
          * @return
          * @throws AuthenticationException
          */
         @Override
         protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
             return null;
         }
     }
     ```

     

4. 创建jsp跳转页面

   - login.jsp: 公共资源
   - Index.jsp：受限资源

5. 启动项目，访问http://localhost:8088/shiro/index.jsp会跳转到http://localhost:8088/shiro/login.jsp页面



## 常见过滤器

注意：shiro提供和多个默认的过滤器，我们可以用这些过滤器来配置控制指定url的权限

| 配置缩写          | 对应的过滤器                   | 功能                                                         |
| ----------------- | ------------------------------ | ------------------------------------------------------------ |
| anon(常用)        | AnonymousFilter                | 指定url可以匿名访问                                          |
| authc(常用)       | FormAuthenticationFilter       | 指定url需要form表单登录，默认会从请求中获取`username`、`password`,`rememberMe`等参数并尝试登录，如果登录不了就会跳转到loginUrl配置的路径。我们也可以用这个过滤器做默认的登录逻辑，但是一般都是我们自己在控制器写登录逻辑的，自己写的话出错返回的信息都可以定制嘛。 |
| authcBasic        | BasicHttpAuthenticationFilter  | 指定url需要basic登录                                         |
| logout            | LogoutFilter                   | 登出过滤器，配置指定url就可以实现退出功能，非常方便          |
| noSessionCreation | NoSessionCreationFilter        | 禁止创建会话                                                 |
| perms             | PermissionsAuthorizationFilter | 需要指定权限才能访问                                         |
| port              | PortFilter                     | 需要指定端口才能访问                                         |
| rest              | HttpMethodPermissionFilter     | 将http请求方法转化成相应的动词来构造一个权限字符串，这个感觉意义不大，有兴趣自己看源码的注释 |
| roles             | RolesAuthorizationFilter       | 需要指定角色才能访问                                         |
| ssl               | SslFilter                      | 需要https请求才能访问                                        |
| user              | UserFilter                     | 需要已登录或“记住我”的用户才能访问                           |

## 认证实现和退出认证

### 认证实现

1. 在login.jsp中开发认证界面

   ```jsp
   <%@page contentType="text/html; UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
   <!doctype html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <meta name="viewport"
             content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
       <meta http-equiv="X-UA-Compatible" content="ie=edge">
       <title>Document</title>
   </head>
   <body>
   
   <h1>用户登录</h1>
   
   <form action="${pageContext.request.contextPath}/user/login" method="post">
       用户名:<input type="text" name="username"> <br/>
       密码 : <input type="text" name="password"> <br>
       <input type="submit" value="登录">
   </form>
   
   </body>
   </html>
   ```

   

2. 开发UserController.java处理身份认证

   ```java
   package com.example.shiro.controller;
   
   import org.apache.shiro.SecurityUtils;
   import org.apache.shiro.authc.IncorrectCredentialsException;
   import org.apache.shiro.authc.UnknownAccountException;
   import org.apache.shiro.authc.UsernamePasswordToken;
   import org.apache.shiro.subject.Subject;
   import org.springframework.stereotype.Controller;
   import org.springframework.web.bind.annotation.RequestMapping;
   
   /**
    * @author jinglv
    * @date 2020/10/04
    */
   @Controller
   @RequestMapping("user")
   public class UserController {
   
       /**
        * 处理身份认证
        *
        * @param username 用户名
        * @param password 密码
        * @return 返回认证信息
        */
       @RequestMapping("login")
       public String login(String username, String password) {
           // 获取主体对象
           Subject subject = SecurityUtils.getSubject();
           try {
               subject.login(new UsernamePasswordToken(username, password));
               return "redirect:/index.jsp";
           } catch (IncorrectCredentialsException e) {
               e.printStackTrace();
               System.out.println("密码错误！");
           } catch (UnknownAccountException e) {
               e.printStackTrace();
               System.out.println("用户名错误！");
           }
           return "redirect:/login.jsp";
       }
   }
   
   ```

   

   - 在认证过程中使用subject.login进行认证

3. 开发UserRealm.java中返回静态数据(未连接数据库)

   ```java
   package com.example.shiro.realm;
   
   import org.apache.shiro.authc.AuthenticationException;
   import org.apache.shiro.authc.AuthenticationInfo;
   import org.apache.shiro.authc.AuthenticationToken;
   import org.apache.shiro.authc.SimpleAuthenticationInfo;
   import org.apache.shiro.authz.AuthorizationInfo;
   import org.apache.shiro.realm.AuthorizingRealm;
   import org.apache.shiro.subject.PrincipalCollection;
   
   /**
    * 自定义Realm
    *
    * @author jinglv
    * @date 2020/10/03
    */
   public class UserRealm extends AuthorizingRealm {
       /**
        * 处理授权
        *
        * @param principalCollection
        * @return
        */
       @Override
       protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
           return null;
       }
   
       /**
        * 处理认证
        *
        * @param authenticationToken
        * @return
        * @throws AuthenticationException
        */
       @Override
       protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
           String principal = (String) authenticationToken.getPrincipal();
           if ("admin".equals(principal)) {
               return new SimpleAuthenticationInfo(principal, "123", this.getName());
           }
           return null;
       }
   }
   ```

   

4. 启动项目以realm中定义静态数据进行认证

   - 请求连接：http://localhost:8088/shiro/login.jsp 输入admin/123正确跳转到index.jsp，则认证成功
   - 认证功能没有md5和随机盐的认证就实现啦

### 退出认证

1. 开发页面退出连接

   ```jsp
   <%@page contentType="text/html; UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
   <%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
   <!doctype html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <meta name="viewport"
             content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
       <meta http-equiv="X-UA-Compatible" content="ie=edge">
       <title>Document</title>
   </head>
   <body>
   <h1>系统主页</h1>
   <a href="">退出登录</a>
   <ul>
       <li><a href="">用户管理</a></li>
       <li><a href="">商品管理</a></li>
       <li><a href="">订单管理</a></li>
       <li><a href="">物流管理</a></li>
   </ul>
   </body>
   </html>
   ```

   

2. UserController中增加退出登录请求

   ```java
   /**
   * 退出登录
   *
   * @return
   */
   @RequestMapping("logout")
   public String logout() {
     // 获取主体对象
     Subject subject = SecurityUtils.getSubject();
     // 退出操作
     subject.logout();
     return "redirect:/login.jsp";
   }
   ```

   

3. 修改退出连接访问退出路径

   ```jsp
   <body>
   <h1>系统主页</h1>
   <a href="${pageContext.request.contextPath}/user/logout">退出登录</a>
   <ul>
       <li><a href="">用户管理</a></li>
       <li><a href="">商品管理</a></li>
       <li><a href="">订单管理</a></li>
       <li><a href="">物流管理</a></li>
   </ul>
   </body>
   ```

   

4. 退出之后访问受限资源立即返回认证界面

   - 请求连接：http://localhost:8088/shiro/login.jsp 输入admin/123正确跳转到index.jsp，则认证成功
   - 在index.jsp界面点击"退出登录"跳转到login.jsp



## MD5、Salt的认证实现

### 开发数据库注册

1. 开发数据库及数据表

   ```sql
   CREATE DATABASE shiro DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
   
   USE shiro;
   
   DROP TABLE IF EXISTS `t_user`;
   CREATE TABLE `t_user`
   (
       `id`       int(6) NOT NULL AUTO_INCREMENT,
       `username` varchar(40)  DEFAULT NULL,
       `password` varchar(40)  DEFAULT NULL,
       `salt`     varchar(255) DEFAULT NULL,
       PRIMARY KEY (`id`)
   ) ENGINE = InnoDB
     AUTO_INCREMENT = 2
     DEFAULT CHARSET = utf8mb4;
   ```

   

2. 项目引入mybatis及mysql相关依赖

   ```xml
   <!--MySQL相关-->
   <dependency>
     <groupId>org.mybatis.spring.boot</groupId>
     <artifactId>mybatis-spring-boot-starter</artifactId>
     <version>2.1.3</version>
   </dependency>
   
   <dependency>
     <groupId>mysql</groupId>
     <artifactId>mysql-connector-java</artifactId>
     <version>8.0.21</version>
   </dependency>
   
   <dependency>
     <groupId>com.alibaba</groupId>
     <artifactId>druid</artifactId>
     <version>1.1.23</version>
   </dependency>
   ```

   

3. 配置数据源相关在application.properties配置文件

   ```properties
   # 数据库配置
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.datasource.url=jdbc:mysql://localhost:3306/shiro?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
   spring.datasource.username=root
   spring.datasource.password=123123
   # MyBaits配置
   mybatis.config-location=classpath:mybatis/mybatis-config.xml
   mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
   mybatis.type-aliases-package=com.example.shiro.entity
   ```

4. 创建User实体类User.java

   ```java
   package com.example.shiro.entity;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   import lombok.experimental.Accessors;
   
   /**
    * @author jinglv
    * @date 2020/10/08
    */
   @Data
   @Accessors(chain = true)
   @AllArgsConstructor
   @NoArgsConstructor
   public class User {
       private String id;
       private String username;
       private String password;
       private String salt;
   }
   ```

   

5. 创建User的DAO类

   ```java
   package com.example.shiro.dao;
   
   import com.example.shiro.entity.User;
   
   /**
    * @author jinglv
    * @date 2020/10/08
    */
   public interface UserDAO {
   
       /**
        * 用户信息保存
        *
        * @param user 用户信息
        */
       void save(User user);
   }
   ```

   

6. 开发mapper配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.example.shiro.dao.UserDAO">
   
       <insert id="save" parameterType="User" useGeneratedKeys="true" keyProperty="id">
           INSERT INTO t_user VALUES (#{id},#{username},#{password},#{salt});
       </insert>
   </mapper>
   ```

   

7. 开发service接口

   ```java
   package com.example.shiro.service;
   
   import com.example.shiro.entity.User;
   
   /**
    * @author jinglv
    * @date 2020/10/08
    */
   public interface UserService {
       /**
        * 用户注册
        *
        * @param user 用户信息
        */
       void register(User user);
   }
   ```

   

8. 创建salt工具类

   ```java
   package com.example.shiro.utils;
   
   import java.util.Random;
   
   /**
    * @author jinglv
    * @date 2020/10/08
    */
   public class SaltUtils {
   
       /**
        * 生成Salt(随机字符)的静态方法
        *
        * @param n 字符长度
        * @return 返回随机盐字符串
        */
       public static String getSalt(int n) {
           char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()".toCharArray();
           StringBuilder sb = new StringBuilder();
           for (int i = 0; i < n; i++) {
               char c = chars[new Random().nextInt(chars.length)];
               sb.append(c);
           }
           return sb.toString();
       }
   
   }
   ```

   

9. 开发service实现类

   ```java
   package com.example.shiro.service.Impl;
   
   import com.example.shiro.dao.UserDAO;
   import com.example.shiro.entity.User;
   import com.example.shiro.service.UserService;
   import com.example.shiro.utils.SaltUtils;
   import org.apache.shiro.crypto.hash.Md5Hash;
   import org.springframework.stereotype.Service;
   
   /**
    * @author jinglv
    * @date 2020/10/08
    */
   @Service
   public class UserServiceImpl implements UserService {
   
       private final UserDAO userDAO;
   
       public UserServiceImpl(UserDAO userDAO) {
           this.userDAO = userDAO;
       }
   
       /**
        * 开发注册功能
        *
        * @param user 用户信息
        */
       @Override
       public void register(User user) {
           // 处理业务调用DAO
           // 1.生成随机盐
           String salt = SaltUtils.getSalt(8);
           // 2.将随机盐保存到数据
           user.setSalt(salt);
           // 3.明文密码进行 md5+salt+hash散列
           Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
           user.setPassword(md5Hash.toHex());
           // 保存到数据库中
           userDAO.save(user);
       }
   }
   
   ```

   注意：这一步，不要忘记加`@Service`注解

10. 开发Controller

    ```java
    package com.example.shiro.controller;
    
    import com.example.shiro.entity.User;
    import com.example.shiro.service.UserService;
    import org.apache.shiro.SecurityUtils;
    import org.apache.shiro.authc.IncorrectCredentialsException;
    import org.apache.shiro.authc.UnknownAccountException;
    import org.apache.shiro.authc.UsernamePasswordToken;
    import org.apache.shiro.subject.Subject;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    
    /**
     * @author jinglv
     * @date 2020/10/04
     */
    @Controller
    @RequestMapping("user")
    public class UserController {
    
        private final UserService userService;
    
        public UserController(UserService userService) {
            this.userService = userService;
        }
    
        /**
         * 用户注册
         *
         * @param user 用户信息
         * @return 接口调用结果
         */
        @RequestMapping("register")
        public String register(User user) {
            try {
                userService.register(user);
                return "redirect:/index.jsp";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/register.jsp";
            }
        }
    }
    
    ```

    

11. 修改启动类并进行项目启动

    ```java
    package com.example.shiro;
    
    import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
    import org.mybatis.spring.annotation.MapperScan;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    
    /**
     * @author jingLv
     * @date 2020/09/23
     */
    @SpringBootApplication(exclude = {ShiroAutoConfiguration.class})
    @MapperScan("com.example.shiro.dao")
    public class SpringBootShiroApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(SpringBootShiroApplication.class, args);
        }
    }
    ```

    注意：在启动类上加Mapper的扫描包，这样就不用每个Mapper（DAO）类上加注解`@Mapper`

12. 访问注册页面http://localhost:8088/shiro/register.jsp，并进行注册，查看数据库

    ![image-20201008142109415](https://gitee.com/JeanLv/study_image2/raw/master///image-20201008142109415.png)



### 开发数据库认证

1. User的DAO类中开发查找用户信息

   ```java
   /**
   * 根据用户名查询用户信息
   *
   * @param username 用户名
   * @return 返回用户信息
   */
   User findByUserName(String username);
   ```

   

2. User的mapper文件中配置对应的数据库查询

   ```xml
   <select id="findByUserName" parameterType="String" resultType="User">
     SELECT id,username,password,salt FROM t_user WHERE username=#{username};
   </select>
   ```

   

3. UserService接口中开发对应的查询用户信息的方法

   ```java
   /**
   * 根据用户名查询用户信息
   *
   * @param username 用户名
   * @return 用户信息
   */
   User findByUserName(String username);
   ```

   

4. 对应的UserService实现类中开发查询用户信息的方法

   ```java
   /**
   * 根据用户名查询用户信息实现
   *
   * @param username 用户名
   * @return 返回用户信息
   */
   @Override
   public User findByUserName(String username) {
     return userDAO.findByUserName(username);
   }
   ```

   

5. 开发在工厂中获取bean对象的工具类

   ```java
   package com.example.shiro.utils;
   
   import org.springframework.beans.BeansException;
   import org.springframework.context.ApplicationContext;
   import org.springframework.context.ApplicationContextAware;
   import org.springframework.stereotype.Component;
   
   /**
    * 开发在工厂中获取bean对象的工具类
    *
    * @author jinglv
    * @date 2020/10/08
    */
   @Component
   public class ApplicationContextUtils implements ApplicationContextAware {
   
       private static ApplicationContext context;
   
       @Override
       public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
           context = applicationContext;
       }
   
       /**
        * 根据Bean的名字获取指定的bean对象
        *
        * @param beanName
        * @return
        */
       public static Object getBean(String beanName) {
           return context.getBean(beanName);
       }
   }
   ```

   

6. 修改自定义realm中的认证处理

   ```java
   /**
   * 处理认证
   *
   * @param authenticationToken
   * @return
   * @throws AuthenticationException
   */
   @Override
   protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
     // 根据身份信息到数据库查询
     String principal = (String) authenticationToken.getPrincipal();
     // 在工厂中获取service对象
     UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
     // 根据获得username到数据库中查询用户信息
     User user = userService.findByUserName(principal);
     if (!ObjectUtils.isEmpty(user)) {
       return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
     }
     return null;
   }
   ```

   

7. 修改ShiroConfig中realm使用凭证匹配器以及hash散列

   ```java
   /**
   * 3. 创建自定义realm
   *
   * @return
   */
   @Bean
   public Realm getRealm() {
     UserRealm userRealm = new UserRealm();
     // 修改凭证校验匹配器
     HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
     // 设置加密算法md5
     credentialsMatcher.setHashAlgorithmName("MD5");
     // 设置散列次数
     credentialsMatcher.setHashIterations(1024);
     userRealm.setCredentialsMatcher(credentialsMatcher);
     return userRealm;
   }
   ```

8. 启动工程，访问登录页面http://localhost:8088/shiro/login.jsp，输入用户名admin/123123，点击【登录】，登录成功

