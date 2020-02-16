package com.code.to.learn.api.api.user;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<UserResponseModel> register(UserBindingModel userBindingModel);

    ResponseEntity<List<UserResponseModel>> findAllUsers();

    ResponseEntity<UserResponseModel> findUser(String username);
}
