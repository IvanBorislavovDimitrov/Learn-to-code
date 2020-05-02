package com.code.to.learn.api.api.user;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServiceApi {

    ResponseEntity<UserResponseModel> register(UserBindingModel userBindingModel);

    ResponseEntity<List<UserResponseModel>> findAllUsers();

    ResponseEntity<UserResponseModel> findUser(String username);

    ResponseEntity<List<UserResponseModel>> findUsersByUsernameContaining(String username);

    ResponseEntity<UserResponseModel> changeUserRoles(String username, List<String> roles);

    ResponseEntity<List<UserResponseModel>> findTeachers();
}
