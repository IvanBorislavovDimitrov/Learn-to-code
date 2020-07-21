package com.code.to.learn.web.config.authentication;

import com.code.to.learn.api.authentication.Authenticator;
import com.code.to.learn.persistence.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuthenticator implements Authenticator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticator.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public DefaultAuthenticator(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public void authenticate(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            getSuccessfulLoginTracker().trackSuccessfulUserLogin(usernamePasswordAuthenticationToken.getName());
        } catch (BadCredentialsException e) {
            LOGGER.error(e.getMessage(), e);
            // TODO: persist failed login requests
            throw e;
        }
    }

    private SuccessfulLoginTracker getSuccessfulLoginTracker() {
        return new SuccessfulLoginTracker(userService);
    }


}
