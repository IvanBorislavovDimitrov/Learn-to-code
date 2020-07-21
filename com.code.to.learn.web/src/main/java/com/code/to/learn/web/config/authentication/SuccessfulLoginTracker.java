package com.code.to.learn.web.config.authentication;

import com.code.to.learn.persistence.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.code.to.learn.web.constants.Messages.USER_SUCCESSFULLY_LOGGED;

public class SuccessfulLoginTracker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuccessfulLoginTracker.class);

    private final UserService userService;

    public SuccessfulLoginTracker(UserService userService) {
        this.userService = userService;
    }

    public void trackSuccessfulUserLogin(String username) {
        try {
            String message = MessageFormat.format(USER_SUCCESSFULLY_LOGGED, username, LocalDate.now(), LocalTime.now());
            userService.storeUserLoginInformation(username, LocalDate.now(), message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
