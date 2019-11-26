package com.code.to.learn.persistence.repository.api;

import com.code.to.learn.persistence.domain.db.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPhoneNumber(String phoneNumber);
}
