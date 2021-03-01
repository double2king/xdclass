package com.def.interceptor;

import com.def.handlerInterceptor.TokenHandlerInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenHandlerInterceptor interceptor = new TokenHandlerInterceptor();
        registry.addInterceptor(interceptor).addPathPatterns("/api/coupon/**").excludePathPatterns("/api/coupon/*/getList");
    }
}
