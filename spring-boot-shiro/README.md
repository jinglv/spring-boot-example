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

   ![image-20200920153913626](https://gitee.com/JeanLv/study_image2/raw/master///image-20200920153913626.png)

3. 