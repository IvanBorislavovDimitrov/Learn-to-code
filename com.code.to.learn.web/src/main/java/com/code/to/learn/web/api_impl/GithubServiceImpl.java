package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.github.GithubService;
import com.code.to.learn.api.model.error.ErrorResponse;
import com.code.to.learn.api.model.github.GithubAccessToken;
import com.code.to.learn.api.model.github.GithubErrorResponse;
import com.code.to.learn.api.model.github.GithubUser;
import com.code.to.learn.core.constant.Constants;
import com.code.to.learn.core.constant.Messages;
import com.code.to.learn.core.environment.Environment;
import com.code.to.learn.core.exception.basic.LCException;
import com.code.to.learn.core.exception.basic.NotFoundException;
import com.code.to.learn.core.exception.github.GithubException;
import com.code.to.learn.core.parser.Parser;
import com.code.to.learn.persistence.domain.model.GithubAccessTokenServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.web.client.ResilientHttpClient;
import com.code.to.learn.web.client.UncheckedEntityUtils;
import com.code.to.learn.web.util.SafeURLDecoder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("githubServiceApiImpl")
public class GithubServiceImpl implements GithubService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubServiceImpl.class);

    private final Parser parser;
    private final ResilientHttpClient resilientHttpClient;
    private final ModelMapper modelMapper;
    private final Environment environment;
    private final UserService userService;

    @Autowired
    public GithubServiceImpl(Parser parser, ResilientHttpClient resilientHttpClient,
                             ModelMapper modelMapper, Environment environment, UserService userService) {
        this.parser = parser;
        this.resilientHttpClient = resilientHttpClient;
        this.modelMapper = modelMapper;
        this.environment = environment;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<GithubUser> getGithubUserInfo(String username) {
        HttpGet userRequest = new HttpGet(getUsernameResource(username));
        HttpResponse userResponse = resilientHttpClient.execute(userRequest);
        if (userResponse.getStatusLine().getStatusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NotFoundException(Messages.USER_NOT_FOUND, username);
        }
        GithubUser githubUser = parser.deserialize(UncheckedEntityUtils.getResponseBody(userResponse), GithubUser.class);
        return ResponseEntity.ok(githubUser);
    }

    @Override
    public ResponseEntity<GithubAccessToken> requestAccessTokenForUser(String loggedUserUsername, String code) {
        Optional<UserServiceModel> optionalUserServiceModel = userService.findByUsername(loggedUserUsername);
        if (!optionalUserServiceModel.isPresent()) {
            throw new GithubException(getFormattedErrorMessage(loggedUserUsername), Collections.emptyMap());
        }
        HttpResponse accessTokenResponse = executeAccessTokenRequest(code);
        return processAccessTokenForUser(optionalUserServiceModel.get(), accessTokenResponse);
    }

    private String getUsernameResource(String username) {
        return environment.getGithubApiUrl() + "/" + com.code.to.learn.web.constants.Constants.USERS_RESOURCE + "/" + username;
    }

    private HttpResponse executeAccessTokenRequest(String code) {
        HttpPost accessTokenRequest = new HttpPost(environment.getGithubAccessTokenUrl());
        List<NameValuePair> parameters = getAccessTokenParameters(code);
        setFormEntity(accessTokenRequest, parameters);
        return resilientHttpClient.execute(accessTokenRequest);
    }

    private List<NameValuePair> getAccessTokenParameters(String code) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(Constants.CLIENT_ID_KEY, environment.getClientIdValue()));
        parameters.add(new BasicNameValuePair(Constants.CLIENT_SECRET_KEY, environment.getClientSecretValue()));
        parameters.add(new BasicNameValuePair(Constants.AUTHENTICATION_CODE, code));
        return parameters;
    }

    private void setFormEntity(HttpPost httpPost, List<NameValuePair> parameters) {
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        } catch (UnsupportedEncodingException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    private ResponseEntity<GithubAccessToken> processAccessTokenForUser(UserServiceModel optionalUserServiceModel, HttpResponse accessTokenResponse) {
        GithubAccessToken githubAccessToken = parseGithubAccessTokenResponse(accessTokenResponse);
        setGithubAccessTokenForUser(optionalUserServiceModel, githubAccessToken);
        return ResponseEntity.ok(githubAccessToken);
    }

    private GithubAccessToken parseGithubAccessTokenResponse(HttpResponse accessTokenResponse) {
        String accessTokenParametersQuery = UncheckedEntityUtils.getResponseBody(accessTokenResponse);
        List<String> rawQueryParameters = Arrays.asList(accessTokenParametersQuery.split("&"));
        Map<String, String> githubAccessTokenQueryParameters = getGithubAccessTokenParameters(rawQueryParameters);
        verifyNotErrorIsReturned(githubAccessTokenQueryParameters);
        return mapToGithubAccessToken(githubAccessTokenQueryParameters);
    }

    private Map<String, String> getGithubAccessTokenParameters(List<String> rawQueryParameters) {
        return rawQueryParameters.stream()
                .map(queryParameter -> queryParameter.split("="))
                .collect(Collectors.toMap(accessTokenKeyValuePair -> accessTokenKeyValuePair[Constants.ACCESS_TOKEN_KEY_POSITION],
                        this::getAccessTokenValueSafely));
    }

    private String getAccessTokenValueSafely(String[] accessTokenKeyValuePair) {
        if (accessTokenKeyValuePair.length != Constants.ACCESS_TOKEN_KEY_VALUE_SIZE) {
            return "";
        }
        return accessTokenKeyValuePair[Constants.ACCESS_TOKEN_VALUE_POSITION];
    }

    public void verifyNotErrorIsReturned(Map<String, String> accessTokenQueryParameters) {
        if (accessTokenQueryParameters.containsKey(Constants.ERROR)) {
            throw new GithubException(com.code.to.learn.web.message.Messages.INVALID_GITHUB_VERIFICATION_CODE, accessTokenQueryParameters);
        }
    }

    private GithubAccessToken mapToGithubAccessToken(Map<String, String> githubAccessTokenQueryParameters) {
        return GithubAccessToken.fromAccessTokenQueryParameters(githubAccessTokenQueryParameters);
    }

    private String getFormattedErrorMessage(String username) {
        ErrorResponse.Builder errorResponse = new ErrorResponse.Builder().code(HttpStatus.BAD_REQUEST.value())
                .message(MessageFormat.format(Messages.USER_WITH_THE_FOLLOWING_USERNAME_NOT_FOUND, username))
                .type(UsernameNotFoundException.class.toString());
        return parser.serialize(errorResponse);
    }

    private void setGithubAccessTokenForUser(UserServiceModel optionalUserServiceModel, GithubAccessToken githubAccessToken) {
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = modelMapper.map(githubAccessToken, GithubAccessTokenServiceModel.class);
        optionalUserServiceModel.setGithubAccessTokenServiceModel(githubAccessTokenServiceModel);
        userService.update(optionalUserServiceModel);
    }

}
