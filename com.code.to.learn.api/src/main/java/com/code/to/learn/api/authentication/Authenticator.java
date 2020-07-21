package com.code.to.learn.api.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface Authenticator {

    void authenticate(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken);
}
