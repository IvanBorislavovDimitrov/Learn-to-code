package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.UserServiceApi;
import com.code.to.learn.api.model.base.ErrorResponse;
import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserViewModel;
import com.code.to.learn.process.exception.user.UserException;
import com.code.to.learn.api.parser.Parser;
import com.code.to.learn.api.parser.ParserFactory;
import com.code.to.learn.api.parser.ParserType;
import com.code.to.learn.process.process.api.UserOperations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplApi implements UserServiceApi {

    private final UserOperations userOperations;
    private final ModelMapper modelMapper;
    private final Parser parser = ParserFactory.createParser(ParserType.JSON);

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
        try {
            userOperations.register(userBindingModel);
            return ResponseEntity.ok()
                    .build();
        } catch (UserException e) {
            return getBadRequestResponseEntity(e);
        }
    }

    private ResponseEntity<?> getBadRequestResponseEntity(UserException excpetion) {
        ErrorResponse.Builder message = new ErrorResponse.Builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .type(excpetion.getClass().getSimpleName())
                .message(excpetion.getMessage());
        return ResponseEntity.badRequest()
                .body(parser.serialize(message));
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
