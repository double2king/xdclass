package com.def.config;

import com.def.handlerInterceptor.TokenHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenHandlerInterceptor interceptor = new TokenHandlerInterceptor();
        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/user/**")
                .excludePathPatterns("/api/user/*/addUser", "/api/user/*/login");
    }
}
