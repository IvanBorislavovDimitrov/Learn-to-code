package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.UserServiceModel;

import java.util.List;

public interface UserService extends GenericService<UserServiceModel> {

    void registerUser(UserServiceModel userServiceModel);

    boolean isUsernameTaken(String username);

    boolean isEmailTaken(String email);

    boolean isPhoneNumberTaken(String phoneNumber);

    UserServiceModel findByUsername(String username);

    long findUsersCount();

    List<UserServiceModel> findUsersByUsernameContaining(String username);

    List<UserServiceModel> findTeachers();

    UserServiceModel activateAccount(String username);

    List<UserServiceModel> findUsersByPage(int page, int maxResults);
}
