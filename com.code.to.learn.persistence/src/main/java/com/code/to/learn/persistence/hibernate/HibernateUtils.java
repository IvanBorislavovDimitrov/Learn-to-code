package com.code.to.learn.persistence.hibernate;

import com.code.to.learn.persistence.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(User.class);

        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
        standardServiceRegistryBuilder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
