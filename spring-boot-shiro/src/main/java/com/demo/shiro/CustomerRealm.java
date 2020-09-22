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
        if ("1231".equals(principal)) {
            // 参数1：返回数据库中的正确的用户名
            // 参数2：返回数据库中的正确的密码
            // 参数3：提供当前realm的名字，使用this.getName()
            return new SimpleAuthenticationInfo(principal, "123123", this.getName());
        }
        return null;
    }
}
