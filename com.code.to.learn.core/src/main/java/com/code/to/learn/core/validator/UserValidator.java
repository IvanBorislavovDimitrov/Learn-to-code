package com.code.to.learn.core.validator;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserChangePasswordBindingModel;
import com.code.to.learn.api.model.user.UserPasswordChangeBindingModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.user.EmailTakenException;
import com.code.to.learn.persistence.exception.user.PasswordsDoNotMatchException;
import com.code.to.learn.persistence.exception.user.PhoneNumberTakenException;
import com.code.to.learn.persistence.exception.user.UsernameTakenException;
import com.code.to.learn.persistence.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserValidator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
            throw new PasswordsDoNotMatchException();
        }
    }

    private boolean passwordsMatch(String password, String confirmPassword) {
        return Objects.equals(password, confirmPassword);
    }

    public void validatePasswordsMatch(UserChangePasswordBindingModel userChangePasswordBindingModel) {
        if (!passwordsMatch(userChangePasswordBindingModel.getPassword(), userChangePasswordBindingModel.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
    }

    public void validatePasswordsMatch(UserPasswordChangeBindingModel userPasswordChangeBindingModel) {
        if (!passwordsMatch(userPasswordChangeBindingModel.getNewPassword(), userPasswordChangeBindingModel.getConfirmNewPassword())) {
            throw new PasswordsDoNotMatchException();
        }
    }

    public void validateCurrentPasswordMatch(String username, String currentPassword) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        if (!passwordEncoder.matches(currentPassword, userServiceModel.getPassword())) {
            throw new PasswordsDoNotMatchException();
        }
    }
}
