package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.User;
import com.code.to.learn.persistence.repository.api.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }
}
