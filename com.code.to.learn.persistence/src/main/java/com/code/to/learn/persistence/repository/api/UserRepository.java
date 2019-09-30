package com.code.to.learn.persistence.repository.api;

import com.code.to.learn.persistence.domain.User;

public interface UserRepository extends GenericRepository<User> {

    User getUserByUsername(String username);
}
