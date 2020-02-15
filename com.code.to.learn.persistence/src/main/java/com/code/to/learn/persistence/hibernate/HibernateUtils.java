package com.code.to.learn.persistence.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.Set;

public final class HibernateUtils {

    private static final SessionFactory SESSION_FACTORY;

    static {
        Configuration configuration = new Configuration().configure();
        Reflections reflections = new Reflections();
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
        entityClasses.forEach(configuration::addAnnotatedClass);
        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
        configuration.configure("hibernate.cfg.xml");
        standardServiceRegistryBuilder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
        SESSION_FACTORY = configuration.buildSessionFactory(serviceRegistry);
    }

    private HibernateUtils() {

    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
