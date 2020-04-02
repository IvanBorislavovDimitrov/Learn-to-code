package com.code.to.learn.web.config;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executors;

@Configuration
@ComponentScan("com.code.to.learn")
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private final ApplicationConfiguration configuration;

    @Autowired
    public AsyncConfig(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    protected WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                configurer.setTaskExecutor(getTaskExecutor());
            }
        };
    }

    @Bean(value = "asyncTaskExecutor")
    protected ConcurrentTaskExecutor getTaskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(configuration.getAsyncExecutorThreadsCount()));
    }

}
