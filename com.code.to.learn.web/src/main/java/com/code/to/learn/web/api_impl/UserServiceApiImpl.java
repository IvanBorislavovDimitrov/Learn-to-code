package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.user.UserService;
import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.core.operation.api.UserOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userServiceApiImpl")
public class UserServiceApiImpl implements UserService {

    private final UserOperations userOperations;

    @Autowired
    public UserServiceApiImpl(UserOperations userOperations) {
        this.userOperations = userOperations;
    }

    @Override
    public ResponseEntity<UserResponseModel> register(UserBindingModel userBindingModel) {
        UserResponseModel registerUser = userOperations.register(userBindingModel);
        return ResponseEntity.ok(registerUser);
    }

    @Override
    public ResponseEntity<List<UserResponseModel>> findAllUsers() {
        List<UserResponseModel> userViewModels = userOperations.findUsers();
        return ResponseEntity.ok().body(userViewModels);
    }

    @Override
    public ResponseEntity<UserResponseModel> findUser(String username) {
        UserResponseModel userResponseModel = userOperations.findByUsername(username);
        return ResponseEntity.ok(userResponseModel);
    }

}
