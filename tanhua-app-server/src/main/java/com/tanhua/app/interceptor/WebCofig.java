package com.tanhua.app.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 17:49
 */

@Configuration
public class WebCofig implements WebMvcConfigurer {

    // 注册TokenInterceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(new TokenInterceptor())
               .addPathPatterns("/**")
               .excludePathPatterns("/user/login", "/user/loginVerification");

    }
}
