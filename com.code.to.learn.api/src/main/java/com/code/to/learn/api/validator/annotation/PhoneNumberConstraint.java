package com.code.to.learn.api.validator.annotation;

import com.code.to.learn.api.constant.Constants;
import com.code.to.learn.api.validator.UserPhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = UserPhoneNumberValidator.class)
public @interface PhoneNumberConstraint {

    String message() default Constants.INVALID_PHONE_NUMBER;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
