package com.code.to.learn.web.bean;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = "com.code.to.learn.core")
public class WebBean {

    private final ApplicationConfiguration configuration;

    @Autowired
    public WebBean(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean(name = "threadPoolExecutor")
    public ExecutorService threadPoolExecutor() {
        return Executors.newFixedThreadPool(configuration.getThreadPoolSize());
    }

}
