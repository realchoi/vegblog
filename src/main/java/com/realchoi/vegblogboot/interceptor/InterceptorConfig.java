package com.realchoi.vegblogboot.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器
 */
@Component
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * 访问拦截器
     */
    private final AccessInterceptor accessInterceptor;

    @Autowired
    public InterceptorConfig(AccessInterceptor accessInterceptor) {
        this.accessInterceptor = accessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(accessInterceptor).excludePathPatterns("/login", "/register", "/error");
    }
}
