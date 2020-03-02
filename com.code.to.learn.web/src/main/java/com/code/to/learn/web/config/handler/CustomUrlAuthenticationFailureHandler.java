package com.code.to.learn.web.config.handler;

import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.SessionFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final SessionFactory sessionFactory;

    public CustomUrlAuthenticationFailureHandler(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        DatabaseSessionUtil.closeSessionWithRollback(sessionFactory);
        super.onAuthenticationFailure(request, response, exception);
    }
}
