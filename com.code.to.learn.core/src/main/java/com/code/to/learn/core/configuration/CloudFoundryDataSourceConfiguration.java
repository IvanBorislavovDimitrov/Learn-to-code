package com.code.to.learn.core.configuration;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.common.PostgresqlServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.persistence.Entity;
import java.util.Properties;
import java.util.Set;

import static com.code.to.learn.persistence.constant.Constants.*;

@Configuration
@ComponentScan(basePackages = "com.code.to.learn.core")
public class CloudFoundryDataSourceConfiguration {

    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public CloudFoundryDataSourceConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @Configuration
    @Profile("cloud")
    public class CloudFoundryDataSourceConfigurationCloud {

        @Bean
        public SessionFactory sessionFactory() {
            CloudFactory cloudFactory = new CloudFactory();
            Cloud cloud = cloudFactory.getCloud();
            PostgresqlServiceInfo postgresqlServiceInfo = (PostgresqlServiceInfo) cloud.getServiceInfo(LEARN_TO_CODE_DATABASE_NAME);

            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
            Reflections reflections = new Reflections();
            Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
            entityClasses.forEach(configuration::addAnnotatedClass);
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            Properties properties = new Properties();
            properties.setProperty(HIBERNATE_CONNECTION_USERNAME, postgresqlServiceInfo.getUserName());
            properties.setProperty(HIBERNATE_CONNECTION_PASSWORD, postgresqlServiceInfo.getPassword());
            properties.setProperty(HIBERNATE_CONNECTION_URL, postgresqlServiceInfo.getJdbcUrl());
            properties.setProperty(HIBERNATE_DIALECT, applicationConfiguration.getHibernateDialect());
            properties.setProperty(HIBERNATE_CONNECTION_DRIVER_CLASS, applicationConfiguration.getHibernatePostgresqlDriver());
            properties.setProperty(HIBERNATE_CONNECTION_POOL_SIZE, applicationConfiguration.getHibernateConnectionPool());
            properties.setProperty(HIBERNATE_HBM_2_DDL_AUTO, applicationConfiguration.getHibernateDDLStrategy());
            properties.setProperty(HIBERNATE_ARCHIVE_AUTODETECTION, applicationConfiguration.getHibernateArchiveAutodetection());
            properties.setProperty(HIBERNATE_SHOW_SQL, applicationConfiguration.hibernateShowSql());
            properties.setProperty(HIBERNATE_DBCP_INITIAL_SIZE, applicationConfiguration.getHibernateDBCPInitialSize());
            properties.setProperty(HIBERNATE_DBCP_MAX_TOTAL, applicationConfiguration.getHibernateDBCPMaxTotal());
            properties.setProperty(HIBERNATE_DBCP_MAX_IDLE, applicationConfiguration.getHibernateDBCPMaxIdle());
            properties.setProperty(HIBERNATE_DBCP_MIN_IDLE, applicationConfiguration.getHibernateDBCPMinIdle());
            properties.setProperty(HIBERNATE_DBCP_MAX_WAIT_MILLIS, applicationConfiguration.getHibernateDBCPMaxWait());
            properties.setProperty(HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS, applicationConfiguration.getHibernateSessionContext());

            configuration.setProperties(properties);

            standardServiceRegistryBuilder.applySettings(configuration.getProperties());
            StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
            return configuration.buildSessionFactory(serviceRegistry);
        }
    }

    @Configuration
    @Profile("dev")
    public class CloudFoundryDataSourceConfigurationDev {

        @Bean
        public SessionFactory sessionFactory() {
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
            Reflections reflections = new Reflections();
            Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
            entityClasses.forEach(configuration::addAnnotatedClass);
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            Properties properties = new Properties();
            properties.setProperty(HIBERNATE_CONNECTION_USERNAME, "postgres");
            properties.setProperty(HIBERNATE_CONNECTION_PASSWORD, "34273427");
            properties.setProperty(HIBERNATE_CONNECTION_URL, "jdbc:postgresql://localhost:5432/code");
            properties.setProperty(HIBERNATE_DIALECT, applicationConfiguration.getHibernateDialect());
            properties.setProperty(HIBERNATE_CONNECTION_DRIVER_CLASS, "org.postgresql.Driver");
            properties.setProperty(HIBERNATE_CONNECTION_POOL_SIZE, "30");
            properties.setProperty(HIBERNATE_HBM_2_DDL_AUTO, "update");
            properties.setProperty(HIBERNATE_ARCHIVE_AUTODETECTION, "class");
            properties.setProperty(HIBERNATE_SHOW_SQL, "true");
            properties.setProperty(HIBERNATE_DBCP_INITIAL_SIZE, "5");
            properties.setProperty(HIBERNATE_DBCP_MAX_TOTAL, "5");
            properties.setProperty(HIBERNATE_DBCP_MAX_IDLE, "3");
            properties.setProperty(HIBERNATE_DBCP_MIN_IDLE, "2");
            properties.setProperty(HIBERNATE_DBCP_MAX_WAIT_MILLIS, "-1");
            properties.setProperty(HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS, applicationConfiguration.getHibernateSessionContext());

            configuration.setProperties(properties);

            standardServiceRegistryBuilder.applySettings(configuration.getProperties());
            StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
            return configuration.buildSessionFactory(serviceRegistry);
        }
    }
}
