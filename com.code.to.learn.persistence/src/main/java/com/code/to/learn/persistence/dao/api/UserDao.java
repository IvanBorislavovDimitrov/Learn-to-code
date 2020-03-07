package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends GenericDao<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findUsersByUsernameContaining(String username);

}
