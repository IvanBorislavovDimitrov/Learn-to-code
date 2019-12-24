package com.code.to.learn.persistence.hibernate;

import com.code.to.learn.persistence.domain.entity.GithubAccessToken;
import com.code.to.learn.persistence.domain.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public final class HibernateUtils {

    private static SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(GithubAccessToken.class);

        StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();
        configuration.configure("hibernate.cfg.xml");
        standardServiceRegistryBuilder.applySettings(configuration.getProperties());
        StandardServiceRegistry serviceRegistry = standardServiceRegistryBuilder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private HibernateUtils() {

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
