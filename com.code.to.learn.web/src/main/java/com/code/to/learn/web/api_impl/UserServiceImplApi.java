package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.UserServiceApi;
import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.api.model.UserViewModel;
import com.code.to.learn.process.process.api.UserOperations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplApi implements UserServiceApi {

    private final UserOperations userOperations;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImplApi(UserOperations userOperations, ModelMapper modelMapper) {
        this.userOperations = userOperations;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<?> sayHello() {
        return ResponseEntity.ok()
                .body("{\"message\":\"hello\"}");
    }

    @Override
    public ResponseEntity<?> register(UserBindingModel userBindingModel) {
        userOperations.register(userBindingModel);
        return ResponseEntity.ok()
                .body("{\"message\":\"done\"}");
    }

    @Override
    public ResponseEntity<List<UserViewModel>> getAllUsers() {
        List<UserViewModel> userViewModels = userOperations.getUsers()
                .stream()
                .map(userServiceModel -> modelMapper.map(userServiceModel, UserViewModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(userViewModels);
    }
}
