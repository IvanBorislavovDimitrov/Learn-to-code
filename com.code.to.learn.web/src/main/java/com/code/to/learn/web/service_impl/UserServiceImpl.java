package com.code.to.learn.web.service_impl;

import com.code.to.learn.api.service.UserService;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class UserServiceImpl implements UserService {

    public void register() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
//        Query insertQuery = entityManager.createNativeQuery("INSERT INTO \"users\"(  \n" +
//                "\"guid\", \"first_name\", \"last_name\", \"email_address\")  \n" +
//                "VALUES (54, 'pedro', 'vtotor', 'asdasdasd@asdasd');");
//        insertQuery.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
