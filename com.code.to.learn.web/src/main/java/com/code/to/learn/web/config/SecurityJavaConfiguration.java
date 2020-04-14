package com.code.to.learn.web.config;

import com.code.to.learn.util.parser.Parser;
import com.code.to.learn.web.config.handler.CustomUrlAuthenticationFailureHandler;
import com.code.to.learn.web.config.handler.CustomUrlAuthenticationSuccessHandler;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.code.to.learn")
public class SecurityJavaConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;
    private final Parser parser;
    private final SessionFactory sessionFactory;

    @Autowired
    public SecurityJavaConfiguration(PasswordEncoder passwordEncoder, @Qualifier("userDetailsService") UserDetailsService userDetailsService,
                                     ModelMapper modelMapper, Parser parser, SessionFactory sessionFactory) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
        this.parser = parser;
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(getRestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/users/logout", "/users/all", "/github/**", "/comments")
                .authenticated()
                .antMatchers("/api/admin/**", "/users/change-roles/**")
                .hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/users/login")
                .successHandler(getSuccessHandler())
                .failureHandler(getFailureHandler())
                .and()
                .logout()
                .invalidateHttpSession(true)
                .permitAll();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod(HttpMethod.OPTIONS);
        config.addAllowedMethod(HttpMethod.HEAD);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.PATCH);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private RestAuthenticationEntryPoint getRestAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    private CustomUrlAuthenticationSuccessHandler getSuccessHandler() {
        return new CustomUrlAuthenticationSuccessHandler(modelMapper, parser, sessionFactory);
    }

    private SimpleUrlAuthenticationFailureHandler getFailureHandler() {
        return new CustomUrlAuthenticationFailureHandler(sessionFactory);
    }

}
