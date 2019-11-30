package com.code.to.learn.api.api;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserViewModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServiceApi {
    ResponseEntity<?> sayHello();

    ResponseEntity<?> register(UserBindingModel userBindingModel);

    ResponseEntity<List<UserViewModel>> getAllUsers();
}
