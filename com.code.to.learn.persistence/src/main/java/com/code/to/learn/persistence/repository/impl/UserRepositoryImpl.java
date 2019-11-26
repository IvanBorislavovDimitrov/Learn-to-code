package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.db.User;
import com.code.to.learn.persistence.repository.api.UserRepository;
import org.springframework.stereotype.Repository;

// REFACTOR AND USE Optional<User>
@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @Override
    public User getUserByUsername(String username) {
        return getByField(User.USERNAME, username);
    }

    @Override
    public User getUserByEmail(String email) {
        return getByField(User.EMAIL, email);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return getByField(User.PHONE_NUMBER, phoneNumber);
    }

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }

}
