package com.code.to.learn.web.config;

import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.core.parser.Parser;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.exception.basic.LCException;
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

    public CustomUrlAuthenticationSuccessHandler(ModelMapper modelMapper, Parser parser) {
        this.modelMapper = modelMapper;
        this.parser = parser;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
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
