package com.code.to.learn.api.validator;

import com.code.to.learn.api.annotation.EmailConstraint;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UserEmailValidator extends RegexValidator implements ConstraintValidator<EmailConstraint, String> {

    private static final String EMAIL_REGEX = "^[A-Za-z][A-Za-z.0-9]+@([A-Za-z]+(\\.)){1,}[A-Za-z0-9]+$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return regexMatches(EMAIL_REGEX, email);
    }
}
