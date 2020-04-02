package com.code.to.learn.web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebServletConfiguration implements WebApplicationInitializer {

    private static final String DISPATCHER_SERVLET = "dispatcher";
    private static final String DEFAULT_MAPPING = "/";
    private static final int DEFAULT_LOAD_ON_STARTUP = 1;

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringConfiguration.class);
        context.setServletContext(container);
        ServletRegistration.Dynamic servlet = container.addServlet(DISPATCHER_SERVLET, new DispatcherServlet(context));
        servlet.setAsyncSupported(true);
        servlet.setLoadOnStartup(DEFAULT_LOAD_ON_STARTUP);
        servlet.addMapping(DEFAULT_MAPPING);
    }
}