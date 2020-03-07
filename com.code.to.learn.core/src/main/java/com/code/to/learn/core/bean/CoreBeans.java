package com.code.to.learn.core.bean;

import com.code.to.learn.core.constant.Constants;
import com.code.to.learn.core.environment.DynamicEnvironment;
import com.code.to.learn.core.environment.Environment;
import com.code.to.learn.core.environment.EnvironmentGetter;
import com.code.to.learn.core.environment.StaticEnvironment;
import com.code.to.learn.util.parser.Parser;
import com.code.to.learn.util.parser.ParserFactory;
import com.code.to.learn.util.parser.ParserType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Configuration
@ComponentScan(value = "com.code.to.learn.core")
public class CoreBeans {

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Environment getEnvironment() {
        if (Objects.nonNull(EnvironmentGetter.getVariable(Constants.USE_DYNAMIC_ENVIRONMENT))) {
            return new DynamicEnvironment();
        }
        return new StaticEnvironment();
    }

    @Bean
    public Parser createParser() {
        if (getEnvironment().useXMLParser()) {
            return ParserFactory.createParser(ParserType.XML);
        }
        return ParserFactory.createParser(ParserType.JSON);
    }
}
