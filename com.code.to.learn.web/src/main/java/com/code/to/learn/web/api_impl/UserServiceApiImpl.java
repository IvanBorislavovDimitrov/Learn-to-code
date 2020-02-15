package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.user.UserService;
import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.core.operation.api.UserOperations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userServiceApiImpl")
public class UserServiceApiImpl implements UserService {

    private final UserOperations userOperations;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceApiImpl(UserOperations userOperations, ModelMapper modelMapper) {
        this.userOperations = userOperations;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<UserResponseModel> register(UserBindingModel userBindingModel) {
        UserResponseModel registerUser = userOperations.register(userBindingModel);
        return ResponseEntity.ok(registerUser);
    }

    @Override
    public ResponseEntity<List<UserResponseModel>> getAllUsers() {
        List<UserResponseModel> userViewModels = userOperations.getUsers().stream()
                .map(userServiceModel -> modelMapper.map(userServiceModel, UserResponseModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(userViewModels);
    }

}
