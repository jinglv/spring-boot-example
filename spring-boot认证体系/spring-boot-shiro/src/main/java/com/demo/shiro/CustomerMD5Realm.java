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
    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("=========");
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
