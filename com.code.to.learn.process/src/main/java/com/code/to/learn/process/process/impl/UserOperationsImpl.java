package com.code.to.learn.process.process.impl;

import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.process.process.api.UserOperations;
import com.code.to.learn.process.validator.UserValidator;
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
    public void register(UserBindingModel userBindingModel) {
        userValidator.validateUserBindingModel(userBindingModel);
        UserServiceModel userServiceModel = toUserServiceModel(userBindingModel);
        userService.registerUser(userServiceModel);
    }

    @Override
    public List<UserServiceModel> getUsers() {
        return userService.getAll();
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
