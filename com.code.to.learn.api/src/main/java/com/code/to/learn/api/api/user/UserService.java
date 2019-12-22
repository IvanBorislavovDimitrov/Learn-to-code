package com.code.to.learn.api.api.user;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserViewModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<?> sayHello();

    ResponseEntity<?> register(UserBindingModel userBindingModel);

    ResponseEntity<List<UserViewModel>> getAllUsers();
}
