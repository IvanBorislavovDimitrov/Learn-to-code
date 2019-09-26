package com.code.to.learn.api.annotations;

import com.code.to.learn.api.constants.Constants;
import com.code.to.learn.api.validator.UserEmailValidator;
import com.code.to.learn.api.validator.UserPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = UserEmailValidator.class)
public @interface EmailConstraint {
    String message() default Constants.INVALID_EMAIL;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
