package com.code.to.learn.web.config;

import com.code.to.learn.core.interceptor.HibernateSessionManagementInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.code.to.learn")
@EnableAsync
public class SpringConfiguration implements WebMvcConfigurer {

    private static final String ALL_ROUTES = "/**";
    private final HibernateSessionManagementInterceptor hibernateSessionManagementInterceptor;

    public SpringConfiguration(HibernateSessionManagementInterceptor hibernateSessionManagementInterceptor) {
        this.hibernateSessionManagementInterceptor = hibernateSessionManagementInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hibernateSessionManagementInterceptor)
                .addPathPatterns(ALL_ROUTES);
    }
}
