package com.code.to.learn.persistence.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResetPasswordTokenGenerator {

    public String generateResetPasswordToken() {
        return UUID.randomUUID().toString();
    }
}
