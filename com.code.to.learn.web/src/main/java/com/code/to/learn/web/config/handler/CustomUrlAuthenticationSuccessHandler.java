package com.code.to.learn.web.config.handler;

import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.core.parser.Parser;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ModelMapper modelMapper;
    private final Parser parser;
    private final SessionFactory sessionFactory;

    public CustomUrlAuthenticationSuccessHandler(ModelMapper modelMapper, Parser parser, SessionFactory sessionFactory) {
        this.modelMapper = modelMapper;
        this.parser = parser;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        DatabaseSessionUtil.closeSessionWithCommit(sessionFactory);
        User user = (User) authentication.getPrincipal();
        UserResponseModel userResponseModel = modelMapper.map(user, UserResponseModel.class);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        setResponseBody(response, userResponseModel);
    }

    private void setResponseBody(HttpServletResponse response, UserResponseModel userResponseModel) {
        try {
            response.getWriter().write(parser.serialize(userResponseModel));
        } catch (IOException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

}
