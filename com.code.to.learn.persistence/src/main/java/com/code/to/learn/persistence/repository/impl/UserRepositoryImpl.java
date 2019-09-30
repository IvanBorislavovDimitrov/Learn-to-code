package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.constant.Constants;
import com.code.to.learn.persistence.domain.User;
import com.code.to.learn.persistence.repository.api.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @Override
    public User getUserByUsername(String username) {
        return getByField(Constants.USERNAME, username, true);
    }

    @Override
    public User getUserByEmail(String email) {
        return getByField(Constants.EMAIL, email, true);
    }

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }

}
