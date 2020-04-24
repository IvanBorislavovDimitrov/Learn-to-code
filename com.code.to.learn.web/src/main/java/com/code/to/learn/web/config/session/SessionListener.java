package com.code.to.learn.web.config.session;

import com.code.to.learn.web.constants.Constants;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setMaxInactiveInterval(Constants.SESSION_TIMEOUT_IN_SECONDS);
    }
}
