package com.code.to.learn.api.validator.annotation;

import com.code.to.learn.api.constant.Constants;
import com.code.to.learn.api.validator.UserBirthDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = UserBirthDateValidator.class)
public @interface NotBefore {

    String date();

    String message() default Constants.INVALID_BIRTH_DATE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
