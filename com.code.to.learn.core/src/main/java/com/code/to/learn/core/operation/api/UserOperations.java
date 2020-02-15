package com.code.to.learn.core.operation.api;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;

import java.util.List;

public interface UserOperations {

    UserResponseModel register(UserBindingModel userBindingModel);

    List<UserResponseModel> getUsers();
}
