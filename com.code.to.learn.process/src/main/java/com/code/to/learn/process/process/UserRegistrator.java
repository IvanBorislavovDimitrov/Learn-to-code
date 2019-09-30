package com.code.to.learn.process.process;

import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.process.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrator {

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Autowired
    public UserRegistrator(ModelMapper modelMapper, UserValidator userValidator) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    public void register(UserBindingModel userBindingModel) {
        userValidator.validateUserBindingModel(userBindingModel);
    }
}
