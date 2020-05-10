package com.code.to.learn.api.api.user;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserChangePasswordBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserServiceApi {

    ResponseEntity<UserResponseModel> register(UserBindingModel userBindingModel);

    ResponseEntity<List<UserResponseModel>> findAllUsers();

    ResponseEntity<UserResponseModel> findUser(String username);

    ResponseEntity<List<UserResponseModel>> findUsersByUsernameContaining(String username);

    ResponseEntity<UserResponseModel> changeUserRoles(String username, List<String> roles);

    ResponseEntity<List<UserResponseModel>> findTeachers();

    ResponseEntity<UserResponseModel> activateAccount(String username);

    ResponseEntity<?> sendEmailForPasswordReset(String username);

    ResponseEntity<UserResponseModel> changeForgottenPassword(UserChangePasswordBindingModel userChangePasswordBindingModel);

    ResponseEntity<UserResponseModel> updateProfilePicture(String username, MultipartFile profilePicture);
}
