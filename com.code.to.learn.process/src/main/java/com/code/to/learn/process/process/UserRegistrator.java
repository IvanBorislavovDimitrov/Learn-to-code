package com.code.to.learn.process.process;

import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.process.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrator {

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrator(ModelMapper modelMapper, UserValidator userValidator, UserService userService, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserBindingModel userBindingModel) {
        userValidator.validateUserBindingModel(userBindingModel);
        UserServiceModel userServiceModel = toUserServiceModel(userBindingModel);
        userService.registerUser(userServiceModel);
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
