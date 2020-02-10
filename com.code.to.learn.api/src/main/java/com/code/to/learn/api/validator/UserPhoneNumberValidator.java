package com.code.to.learn.api.validator;

import com.code.to.learn.api.validator.annotation.PhoneNumberConstraint;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Component
public class UserPhoneNumberValidator extends RegexValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

    private static final String PHONE_NUMBER_REGEX = "^\\+\\d{12}$|^\\d{10}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(phoneNumber) && regexMatches(PHONE_NUMBER_REGEX, phoneNumber);

    }
}
