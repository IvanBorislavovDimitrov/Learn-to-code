package com.code.to.learn.core.validator;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.exception.user.UserException;
import com.code.to.learn.persistence.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    public void validateUserBindingModel(UserBindingModel userBindingModel) {
        validateUsernameNotTaken(userBindingModel);
        validateEmailNotTaken(userBindingModel);
        validatePhoneNumberNotTaken(userBindingModel);
        validatePasswordsMatch(userBindingModel);
    }

    private void validateUsernameNotTaken(UserBindingModel userBindingModel) {
        if (userService.isUsernameTaken(userBindingModel.getUsername())) {
            throw new UserException(Messages.USERNAME_IS_TAKEN, userBindingModel.getUsername());
        }
    }

    private void validateEmailNotTaken(UserBindingModel userBindingModel) {
        if (userService.isEmailTaken(userBindingModel.getEmail())) {
            throw new UserException(Messages.EMAIL_IS_TAKEN, userBindingModel.getEmail());
        }
    }

    private void validatePhoneNumberNotTaken(UserBindingModel userBindingModel) {
        if (userService.isPhoneNumberTaken(userBindingModel.getPhoneNumber())) {
            throw new UserException(Messages.PHONE_NUMBER_IS_TAKEN, userBindingModel.getPhoneNumber());
        }
    }

    private void validatePasswordsMatch(UserBindingModel userBindingModel) {
        if (!passwordsMatch(userBindingModel.getPassword(), userBindingModel.getConfirmPassword())) {
            throw new UserException(Messages.PASSWORDS_DO_NOT_MATCH);
        }
    }

    private boolean passwordsMatch(String password, String confirmPassword) {
        return Objects.equals(password, confirmPassword);
    }
}
