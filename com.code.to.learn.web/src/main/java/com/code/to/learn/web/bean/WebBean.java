package com.code.to.learn.web.bean;

import com.code.to.learn.web.constants.Constants;
import com.code.to.learn.web.environment.DynamicEnvironment;
import com.code.to.learn.web.environment.Environment;
import com.code.to.learn.web.environment.EnvironmentGetter;
import com.code.to.learn.web.environment.StaticEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class WebBean {

    @Bean
    public Environment getEnvironment() {
        if (Objects.nonNull(EnvironmentGetter.getVariable(Constants.USE_DYNAMIC_ENVIRONMENT))) {
            return new DynamicEnvironment();
        }
        return new StaticEnvironment();
    }
}
