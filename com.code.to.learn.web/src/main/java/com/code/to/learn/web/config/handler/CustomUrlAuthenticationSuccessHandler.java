package com.code.to.learn.web.config.handler;

import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.parser.Parser;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.code.to.learn.web.constants.Messages.USER_SUCCESSFULLY_LOGGED;

public class CustomUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUrlAuthenticationSuccessHandler.class);

    private final ModelMapper modelMapper;
    private final Parser parser;
    private final UserService userService;

    public CustomUrlAuthenticationSuccessHandler(ModelMapper modelMapper, Parser parser, UserService userService) {
        this.modelMapper = modelMapper;
        this.parser = parser;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserResponseModel userResponseModel = modelMapper.map(user, UserResponseModel.class);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        setResponseBody(response, userResponseModel);
        trackSuccessfulUserLogin(user.getUsername());
    }

    private void setResponseBody(HttpServletResponse response, UserResponseModel userResponseModel) {
        try {
            response.getWriter().write(parser.serialize(userResponseModel));
        } catch (IOException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    private void trackSuccessfulUserLogin(String username) {
        try {
            userService.storeUserLoginInformation(username, LocalDate.now(), MessageFormat.format(USER_SUCCESSFULLY_LOGGED,
                    username, LocalDate.now(), LocalTime.now()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
