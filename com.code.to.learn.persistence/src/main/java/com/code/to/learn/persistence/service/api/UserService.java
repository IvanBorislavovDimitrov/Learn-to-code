package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.UserServiceModel;

public interface UserService extends Service<UserServiceModel> {

    void registerUser(UserServiceModel userServiceModel);

    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);
}
