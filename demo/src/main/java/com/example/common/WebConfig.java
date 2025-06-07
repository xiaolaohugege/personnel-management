package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login"
                        , "/user/register"
                        , "/user/updataSalary"
                        ,"/admin/export"
                        ,"/admin/import"
                        ,"/file/download/**"
                        ,"/file/upload"
                        ,"/user/export"
                );
}

    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }

}
