package com.code.to.learn.api.validator;

import com.code.to.learn.api.annotations.PasswordConstraint;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UserPasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password.contains("!") ||
                password.contains("@") ||
                password.contains("#") ||
                password.contains("$") ||
                password.contains("%") ||
                password.contains("^") ||
                password.contains("&") ||
                password.contains("*");
    }
}
