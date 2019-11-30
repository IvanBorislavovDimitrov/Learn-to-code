package com.code.to.learn.process.process.api;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;

import java.util.List;

public interface UserOperations {
    void register(UserBindingModel userBindingModel);

    List<UserServiceModel> getUsers();
}
