package com.code.to.learn.api.api;

import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.api.model.UserViewModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServiceApi {
    ResponseEntity<?> sayHello();

    ResponseEntity<?> register(UserBindingModel userBindingModel);

    ResponseEntity<List<UserViewModel>> getAllUsers();
}
