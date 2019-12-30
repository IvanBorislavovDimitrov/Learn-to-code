package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.user.UserService;
import com.code.to.learn.api.model.error.ErrorResponse;
import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserViewModel;
import com.code.to.learn.core.exception.user.UserException;
import com.code.to.learn.core.operation.api.UserOperations;
import com.code.to.learn.core.parser.Parser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userServiceApiImpl")
public class UserServiceImpl implements UserService {

    private final UserOperations userOperations;
    private final ModelMapper modelMapper;
    private final Parser parser;

    @Autowired
    public UserServiceImpl(UserOperations userOperations, ModelMapper modelMapper, Parser parser) {
        this.userOperations = userOperations;
        this.modelMapper = modelMapper;
        this.parser = parser;
    }

    @Override
    public ResponseEntity<?> sayHello() {
        return ResponseEntity.ok().body("{\"message\":\"hello\"}");
    }

    @Override
    public ResponseEntity<?> register(UserBindingModel userBindingModel) {
        try {
            userOperations.register(userBindingModel);
            return ResponseEntity.ok().build();
        } catch (UserException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    private ResponseEntity<?> getBadRequestResponseEntity(UserException exception) {
        ErrorResponse.Builder errorResponse = new ErrorResponse.Builder().code(HttpStatus.BAD_REQUEST.value())
                .type(exception.getClass().getSimpleName()).message(exception.getMessage());
        return ResponseEntity.badRequest().body(parser.serialize(errorResponse));
    }

    @Override
    public ResponseEntity<List<UserViewModel>> getAllUsers() {
        List<UserViewModel> userViewModels = userOperations.getUsers().stream()
                .map(userServiceModel -> modelMapper.map(userServiceModel, UserViewModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(userViewModels);
    }

}
