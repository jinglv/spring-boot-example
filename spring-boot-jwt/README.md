# JWT

JSON Web令牌

## JWT介绍

[jwt官方网站](https://jwt.io/introduction/)

json web token(jwt)是一个开放标准(RFC 7519)，它定义了一种紧凑、自包含的方式，用于在各方之间以JSON对象安全地址传输信息，此信息可以验证和信任，因为它是数字签名的。jwt可以使用秘密（使用HMAC算法）或使用RSA或ECDSA的公钥/私钥对进行签名。



通俗解释

JSON Web Token简称JWT，也就是通过JSON形式作为Web应用中的令牌，用于在各方之间安全地将信息作为JSON对象传输，在数据传输过程中还可以完成数据加密，签名等相关处理。

## JWT能做什么

### 授权

这是使用JWT的最常见方案，一旦用户登录，每个后续请求包括JWT，从而允许用户访问该令牌允许的路由，服务和资源，单点登录是当今广泛使用JWT的一项功能，因为它的开销很小并且可以在不同的域中轻松使用。

### 信息交换

JSON Web Token是在各方之间安全传输信息的好方法，因为可以对JWT进行签名（例如，使用公钥/私钥对），所以您可以确保发件人是他们所说的人，此外，由于签名是使用标头和有效负载计算，因此您还可以验证内容是否遭到篡改。



## 为什么是JWT

### 基于传统的Session认证

1. 认证方式

   http协议本身是一种无状态的协议，而这意味着如果用户向我们的应用提供了用户名和密码来进行用户认证，那么下一次请求时，用户还要再一次进行用户认证才行，因为根据http协议，我们并不能知道是哪个用户发出的请求，所以为了让我们的应用能识别是哪个用户发出的请求，我们只能在服务器存储一份用户登录的信息，这份登录信息会在响应时传输给浏览器，告诉其保存为cookie，以便下次请求时发送给我们的应用，这样我们的应用就能识别请求来自哪个用户了，这就是传统的基于session认证。

2. 认证流程

   ![image-20200929103053147](https://gitee.com/JeanLv/study_image/raw/master///image-20200929103053147.png)

3. 暴露问题

   - 每个用户经过我们的应用认证之后，我们的应用都要在服务端做一次记录，以方便用户下次请求鉴别，通常而言session都是保存在内存中，而随着认证用户的增多，服务端的开销会明显增大。

   - 用户认证之后，服务端做认证记录，如果认证的记录被保存在内存中的话，这意味着用户下次请求还必须请求在这台服务器上，这样才能拿到授权的资源，这样在分布式的应用上，相应的限制了负载均衡的能力，这也意味着限制了应用的扩展能力。

   - 因为是基于cookie来进行用户识别的，cookie如果被截获，用户会很容易受到跨站请求伪造的攻击。

   - 在前后端分离系统中就更加痛苦，如下图所示

     ![image-20200929103407106](https://gitee.com/JeanLv/study_image/raw/master///image-20200929103407106.png)

     也就是说前后端分离在应用解耦合增加了部署的复杂性。通常用户一次请求就要转发多次，如果session每次携带sessionId到服务器，服务器还要查询用户信息。同时如果用户很多，这些信息就会存储在服务器内存中，给服务器增加负担。还有CSRF（跨站伪造请求攻击），session是基于cookie进行用户识别的，cookie如果被截获，用户就会很容易受到跨站请求伪造的攻击。还有就是sessionId就是一个特征值，表达的信息不够丰富。不容易扩展，而且如果你后端应用是多节点部署。那么就需要实现session共享机制，不方便集群应用。

### 基于JWT认证

![image-20200929104516694](https://gitee.com/JeanLv/study_image/raw/master///image-20200929104516694.png)

1. 认证流程
   - 首先，前端通过Web表单将自己的用户名和密码发送到后端的接口。这一过程一般是一个HTTP POST请求。建议的方式是通过SSL加密的传输（https协议），从而避免敏感信息被嗅探。
   - 后端核对用户名和密码成功后，将用户的id等其他信息作为JWT Payload（负载），将其与头部分别进行Base64编码拼接后签名，形成一个JWT(Token)。形成的JWT就是一个形同lll.zzz.xxx的字符串。` token head.payload.signature`
   - 后端将JWT字符串作为登录成功的返回结果返回给前端。前端可以将返回的结果保存在localStorage或sessionStorage上，退出登录时前端删除保存的JWT即可。
   - 前端在每次请求时将JWT放入HTTP Header中的Authorization位。(解决XSS和XSRF问题) HEADER
   - 后端检查是否存在，如存在验证JWT的有效性。例如，检查签名是否正确；检查Token是否过期；检查Token的接收方是否是自己（可选）。
   - 验证通过后后端使用JWT中包含的用户信息进行其他逻辑操作，返回相应结果。
2. jwt优势
   - 简洁(Compact): 可以通过URL，POST参数或者在HTTP header发送，因为数据量小，传输速度也很快
   - 自包含(Self-contained)：负载中包含了所有用户所需要的信息，避免了多次查询数据库
   - 因为Token是以JSON加密的形式保存在客户端的，所以JWT是跨语言的，原则上任何web形式都支持
   - 不需要在服务端保存会话信息，特别适用于分布式微服务



## JWT的结构

token String ===> header.payload.signature

1. 令牌组成

   - 标头（Header）
   - 有效载荷（Payload）
   - 签名（signature）

   因此，JWT通常如下：xxxxx.yyyyy.zzzzz   Header.Payload.Signature

2. Header

   - 标头通常由两部分组成：令牌的类型（即JWT）和所使用的签名算法，例如HMAC SHA256或RSA。它会使用 Base64 编码组成 JWT 结构的第一部分。
   - 注意:Base64是一种编码，也就是说，它是可以被翻译回原来的样子来的。它并不是一种加密过程

   ```json
   {
     "alg": "HS256",
     "typ": "JWT"
   }
   ```

   对Json串进行Base64的编码，之后使用还可以进行解码

3. Payload

   - 令牌的第二部分是有效负载，其中包含声明。声明是有关实体（通常是用户）和其他数据的声明。同样的，它会使用 Base64 编码组成 JWT 结构的第二部分。

   ```json
   {
     "sub": "1234567890",
     "name": "John Doe",
     "admin": true
   }
   ```

   用户信息放到载荷中，也需要进行Base64编码，传递后可进行Base64解码，放用户名、用户id即可，不要放密码等敏感信息

4. Signature

   - 前面两部分都是使用 Base64 进行编码的，即前端可以解开知道里面的信息。Signature 需要使用编码后的 header 和 payload 以及我们提供的一个密钥，然后使用 header 中指定的签名算法（HS256）进行签名。签名的作用是保证 JWT 没有被篡改过。

   - 如：`HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload),secret);`

   - 签名目的

     - 最后一步签名的过程，实际上是**对头部以及负载内容进行签名，防止内容被窜改**。如果有人对头部以及负载的内容解码之后进行修改，再进行编码，最后加上之前的签名组合形成新的JWT的话，那么服务器端会判断出新的头部和负载形成的签名和JWT附带上的签名是不一样的。如果要对新的头部和负载进行签名，在不知道服务器加密时用的密钥的话，得出来的签名也是不一样的。

   - 信息安全问题

     - 在这里大家一定会问一个问题：Base64是一种编码，是可逆的，那么我的信息不就被暴露了吗？

     - 是的。所以，在JWT中，不应该在**负载里面加入任何敏感的数据**。在上面的例子中，我们传输的是用户的User ID。这个值实际上不是什么敏	感内容，一般情况下被知道也是安全的。但是像密码这样的内容就不能被放在JWT中了。如果将用户的密码放在了JWT中，那么怀有恶意的第	三方通过Base64解码就能很快地知道你的密码了。因此JWT适合用于向Web应用传递一些非敏感信息。JWT还经常用于设计用户认证和授权系	统，甚至实现Web应用的单点登录。

       

       ![image-20200929191951038](../../../Library/Application Support/typora-user-images/image-20200929191951038.png)

5. 组合一起

- 输出是三个由点分隔的Base64-URL字符串，可以在HTML和HTTP环境中轻松传递这些字符串，与基于XML的标准（例如SAML）相比，它更紧凑。

- 简洁(Compact)
	可以通过URL, POST 参数或者在 HTTP header 发送，因为数据量小，传输速度快
- 自包含(Self-contained)
	负载中包含了所有用户所需要的信息，避免了多次查询数据库



JWT生成的示例：

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.
eyJleHAiOjE2MDEzNzkzMDAsInVzZXJJZCI6IjEwMDEiLCJ1c2VybmFtZSI6ImFkbWluIn0.
xTOd7AFJ-vezVbq6zHGOk2fFJFMYuH6lqvgTa8Wp6u8
```





## 使用JWT

1. 引入依赖

   ```xml
   <!-- https://mvnrepository.com/artifact/com.auth0/java-jwt -->
   <dependency>
     <groupId>com.auth0</groupId>
     <artifactId>java-jwt</artifactId>
     <version>${jwt.version}</version>
   </dependency>
   ```

   

2. 生成token

3. 根据令牌和签名解析数据

   ```java
   package com.demo.jwt;
   
   import com.auth0.jwt.JWT;
   import com.auth0.jwt.JWTVerifier;
   import com.auth0.jwt.algorithms.Algorithm;
   import com.auth0.jwt.interfaces.DecodedJWT;
   
   import java.util.Calendar;
   import java.util.HashMap;
   
   /**
    * JWT示例
    *
    * @author jingLv
    * @date 2020/09/29
    */
   public class TestJwt {
   
       public static void main(String[] args) {
           Calendar instance = Calendar.getInstance();
           instance.add(Calendar.SECOND, 90);
           // 1. 生成token
           HashMap<String, Object> map = new HashMap<>();
           String token = JWT.create()
                   // header
                   .withHeader(map)
                   // payload
                   .withClaim("userId", 1001)
                   .withClaim("username", "admin")
                   // 指定过期时间
                   .withExpiresAt(instance.getTime())
                   // 设置签名，保密，复杂
                   .sign(Algorithm.HMAC256("token!Q2W#E$RW"));
   
           System.out.println(token);
   
           // 2. 根据令牌和签名解析数据
           // 创建验证对象
           JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("token!Q2W#E$RW")).build();
           DecodedJWT decodedJwt = jwtVerifier.verify(token);
   
           System.out.println("用户ID: " + decodedJwt.getClaim("userId").asInt());
           System.out.println("用户名: " + decodedJwt.getClaim("username").asString());
           System.out.println("过期时间: " + decodedJwt.getExpiresAt());
       }
   }
   ```

   执行结果

   ```java
   eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDEzNzk5MzAsInVzZXJJZCI6MTAwMSwidXNlcm5hbWUiOiJhZG1pbiJ9.pKDSKOTdHceMx5NiIe0O0Ye4mnz6CtLosp0rhrewFqQ
   用户ID: 1001
   用户名: admin
   过期时间: Tue Sep 29 19:45:30 CST 2020
   ```

   

4. 常见异常信息

   - SignatureVerificationException:				签名不一致异常
   - TokenExpiredException:    						令牌过期异常
   - AlgorithmMismatchException:				  算法不匹配异常
   - InvalidClaimException:								失效的payload异常



## JWT的工具类封装

```java
package com.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * JWT工具类的封装
 *
 * @author jingLv
 * @date 2020/09/29
 */
public class JWTUtils {

    private static final String SING = "!Q@W3e4r";

    /**
     * 生成token
     * header.payload.signature
     *
     * @param map header.payload.signature
     * @return 返回token
     */
    public static String getToken(Map<String, String> map) {
        // 默认过期时间7天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);
        // 生成令牌
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        // 指定过期时间和设置签名
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));
    }

    /**
     * 验证token合法性
     *
     * @param token 需要验证的token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * 获取token信息
     *
     * @param token 生成的token
     * @return 返回已获取的信息
     */
    public static DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
}
```

