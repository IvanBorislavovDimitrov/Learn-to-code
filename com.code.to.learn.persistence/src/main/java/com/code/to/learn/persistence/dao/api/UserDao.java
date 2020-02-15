package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.User;
import org.hibernate.Session;

import java.util.Optional;

public interface UserDao extends GenericDao<User> {

    Optional<User> findByUsername(String username, Session session);

    Optional<User> findByEmail(String email, Session session);

    Optional<User> findByPhoneNumber(String phoneNumber, Session session);

    long findUsersCount(Session session);
}
