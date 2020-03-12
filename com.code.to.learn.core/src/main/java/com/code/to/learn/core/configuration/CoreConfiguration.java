package com.code.to.learn.core.configuration;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.code.to.learn.util.parser.Parser;
import com.code.to.learn.util.parser.ParserFactory;
import com.code.to.learn.util.parser.ParserType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = "com.code.to.learn.core")
public class CoreConfiguration {

    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public CoreConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Parser createParser() {
        return ParserFactory.createParser(ParserType.valueOf(applicationConfiguration.getParserType()));
    }

}
