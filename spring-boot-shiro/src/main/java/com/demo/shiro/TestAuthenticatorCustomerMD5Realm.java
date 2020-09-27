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

        // 认证用户进行判断 
        if (subject.isAuthenticated()) {
            // 基于角色的权限控制
            boolean admin = subject.hasRole("admin");
            System.out.println(admin);
        }
    }
}
