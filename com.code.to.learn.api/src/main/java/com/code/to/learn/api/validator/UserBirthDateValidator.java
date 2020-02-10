package com.code.to.learn.api.validator;

import com.code.to.learn.api.validator.annotation.NotBefore;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Component
public class UserBirthDateValidator implements ConstraintValidator<NotBefore, String> {

    private String notBefore;

    @Override
    public void initialize(NotBefore constraintAnnotation) {
        notBefore = constraintAnnotation.date();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return false;
        }
        LocalDate birthDate = LocalDate.parse(date);
        LocalDate notBeforeDate = LocalDate.parse(notBefore);
        return birthDate.compareTo(notBeforeDate) > 0;
    }
}
