package com.code.to.learn.api.api;

import com.code.to.learn.api.model.UserBindingModel;
import org.springframework.http.ResponseEntity;

public interface UserServiceApi {
    ResponseEntity<?> sayHello();

    ResponseEntity<?> register(UserBindingModel userBindingModel);

    ResponseEntity<?> getAllUsers();
}
