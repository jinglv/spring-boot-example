package com.example.jwt.config;

import com.example.jwt.interceptors.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jingLv
 * @date 2020/09/30
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor()).
                // 接口需要token验证
                        addPathPatterns("/user/test").
                // 接口不需要token验证
                        excludePathPatterns("/user/login");
    }
}
