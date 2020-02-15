package com.code.to.learn.core.operation.impl;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.core.operation.api.UserOperations;
import com.code.to.learn.core.validator.UserValidator;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserOperationsImpl implements UserOperations {

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserOperationsImpl(ModelMapper modelMapper, UserValidator userValidator, UserService userService, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseModel register(UserBindingModel userBindingModel) {
        userValidator.validateUserBindingModel(userBindingModel);
        UserServiceModel userServiceModel = toUserServiceModelWithEncodedPassword(userBindingModel);
        userService.registerUser(userServiceModel);
        return modelMapper.map(userServiceModel, UserResponseModel.class);
    }

    @Override
    public List<UserResponseModel> getUsers() {
        return userService.findAll().stream()
                .map(userServiceModel -> modelMapper.map(userServiceModel, UserResponseModel.class))
                .collect(Collectors.toList());
    }

    private UserServiceModel toUserServiceModelWithEncodedPassword(UserBindingModel userBindingModel) {
        UserBindingModel encodedPasswordUserBindingModel = getUserBindingModelWithEncodedPassword(userBindingModel);
        return modelMapper.map(encodedPasswordUserBindingModel, UserServiceModel.class);
    }

    private UserBindingModel getUserBindingModelWithEncodedPassword(UserBindingModel userBindingModel) {
        UserBindingModel encodedPasswordUserBindingModel = new UserBindingModel(userBindingModel);
        String planePassword = userBindingModel.getPassword();
        String encodedPassword = passwordEncoder.encode(planePassword);
        encodedPasswordUserBindingModel.setPassword(encodedPassword);
        encodedPasswordUserBindingModel.setConfirmPassword(encodedPassword);
        return encodedPasswordUserBindingModel;
    }
}
