package com.code.to.learn.persistence.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.reflections.Reflections;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.persistence.Entity;
import java.util.Properties;
import java.util.Set;

@Configuration
@Profile("cloud")
public class CloudFoundryDataSourceConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        CloudFactory cloudFactory = new CloudFactory();
        Cloud cloud = cloudFactory.getCloud();
        PostgresqlServiceInfo postgresqlServiceInfo = (PostgresqlServiceInfo) cloud.getServiceInfo("learn-to-code-database");

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
        Reflections reflections = new Reflections();
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
        entityClasses.forEach(configuration::addAnnotatedClass);
        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.username", postgresqlServiceInfo.getUserName());
        properties.setProperty("hibernate.connection.password", postgresqlServiceInfo.getPassword());
        properties.setProperty("hibernate.connection.url", postgresqlServiceInfo.getJdbcUrl());
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.pool_size", "3");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.archive.autodetection", "class");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.dbcp.initialSize", "2");
        properties.setProperty("hibernate.dbcp.maxTotal", "3");
        properties.setProperty("hibernate.dbcp.maxIdle", "3");
        properties.setProperty("hibernate.dbcp.minIdle", "0");
        properties.setProperty("hibernate.dbcp.maxWaitMillis", "-1");
        properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");

        configuration.setProperties(properties);

        standardServiceRegistryBuilder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
