package com.code.to.learn.core.operation.impl;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserGenericService;
import com.code.to.learn.core.operation.api.UserOperations;
import com.code.to.learn.core.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOperationsImpl implements UserOperations {

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final UserGenericService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserOperationsImpl(ModelMapper modelMapper, UserValidator userValidator, UserGenericService userService, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseModel register(UserBindingModel userBindingModel) {
        userValidator.validateUserBindingModel(userBindingModel);
        UserServiceModel userServiceModel = toUserServiceModel(userBindingModel);
        userService.registerUser(userServiceModel);
        return modelMapper.map(userServiceModel, UserResponseModel.class);
    }

    @Override
    public List<UserServiceModel> getUsers() {
        return userService.findAll();
    }

    private UserServiceModel toUserServiceModel(UserBindingModel userBindingModel) {
        encodePassword(userBindingModel);
        return modelMapper.map(userBindingModel, UserServiceModel.class);
    }

    private void encodePassword(UserBindingModel userBindingModel) {
        String planePassword = userBindingModel.getPassword();
        String encodedPassword = passwordEncoder.encode(planePassword);
        userBindingModel.setPassword(encodedPassword);
        userBindingModel.setConfirmPassword(encodedPassword);
    }
}
