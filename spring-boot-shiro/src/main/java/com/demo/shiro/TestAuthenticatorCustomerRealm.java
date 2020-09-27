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
