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
