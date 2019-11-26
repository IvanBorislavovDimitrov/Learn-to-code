package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.db.User;
import com.code.to.learn.persistence.repository.api.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @Override
    public Optional<User> findUserByUsername(String username) {
        return findByField(User.USERNAME, username);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return findByField(User.EMAIL, email);
    }

    @Override
    public Optional<User> findUserByPhoneNumber(String phoneNumber) {
        return findByField(User.PHONE_NUMBER, phoneNumber);
    }

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }

}
