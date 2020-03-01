package com.code.to.learn.persistence.service.api;

import java.util.Optional;

import com.code.to.learn.persistence.domain.model.UserServiceModel;

public interface UserService extends GenericService<UserServiceModel> {

    void registerUser(UserServiceModel userServiceModel);

    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);

    boolean isPhoneNumberTaken(String phoneNumber);

    Optional<UserServiceModel> findByUsername(String username);

    long findUsersCount();
}
