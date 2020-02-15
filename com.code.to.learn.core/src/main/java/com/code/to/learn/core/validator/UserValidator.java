package com.code.to.learn.core.validator;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.core.exception.user.EmailTakenException;
import com.code.to.learn.core.exception.user.PasswordsNotMatchException;
import com.code.to.learn.core.exception.user.PhoneNumberTakenException;
import com.code.to.learn.core.exception.user.UsernameTakenException;
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
            throw new UsernameTakenException(userBindingModel.getUsername());
        }
    }

    private void validateEmailNotTaken(UserBindingModel userBindingModel) {
        if (userService.isEmailTaken(userBindingModel.getEmail())) {
            throw new EmailTakenException(userBindingModel.getEmail());
        }
    }

    private void validatePhoneNumberNotTaken(UserBindingModel userBindingModel) {
        if (userService.isPhoneNumberTaken(userBindingModel.getPhoneNumber())) {
            throw new PhoneNumberTakenException(userBindingModel.getPhoneNumber());
        }
    }

    private void validatePasswordsMatch(UserBindingModel userBindingModel) {
        if (!passwordsMatch(userBindingModel.getPassword(), userBindingModel.getConfirmPassword())) {
            throw new PasswordsNotMatchException();
        }
    }

    private boolean passwordsMatch(String password, String confirmPassword) {
        return Objects.equals(password, confirmPassword);
    }
}
