package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.repository.api.RoleRepository;
import com.code.to.learn.persistence.repository.api.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    private final RoleRepository roleRepository;

    @Autowired
    public UserRepositoryImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

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
    public void persist(User entity) {

        super.persist(entity);
    }

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }

}
