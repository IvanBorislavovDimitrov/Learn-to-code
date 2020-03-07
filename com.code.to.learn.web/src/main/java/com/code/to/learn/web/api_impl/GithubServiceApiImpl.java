package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.github.GithubServiceApi;
import com.code.to.learn.api.model.github.GithubAccessTokenResponseModel;
import com.code.to.learn.api.model.github.GithubUserResponseModel;
import com.code.to.learn.core.constant.Constants;
import com.code.to.learn.core.environment.Environment;
import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.domain.model.GithubAccessTokenServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.code.to.learn.persistence.exception.github.GithubException;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.util.parser.Parser;
import com.code.to.learn.web.client.ResilientHttpClient;
import com.code.to.learn.web.client.UncheckedEntityUtils;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GithubServiceApiImpl extends ExtendableMapper<GithubAccessTokenServiceModel, GithubAccessTokenResponseModel> implements GithubServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubServiceApiImpl.class);

    private final Parser parser;
    private final ResilientHttpClient resilientHttpClient;
    private final Environment environment;
    private final UserService userService;

    @Autowired
    public GithubServiceApiImpl(Parser parser, ResilientHttpClient resilientHttpClient,
                                ModelMapper modelMapper, Environment environment, UserService userService) {
        super(modelMapper);
        this.parser = parser;
        this.resilientHttpClient = resilientHttpClient;
        this.environment = environment;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<GithubUserResponseModel> getGithubUserInfo(String username) {
        UserServiceModel userServiceModel = getRequiredUser(username);
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = userServiceModel.getGithubAccessToken();
        if (githubAccessTokenServiceModel == null) {
            throw new GithubException(MessageFormat.format(Messages.NO_GITHUB_ACCESS_TOKEN_FOR_USER, username));
        }
        HttpGet userRequest = new HttpGet(getUsernameResource());
        userRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubAccessTokenServiceModel.getAccessToken());
        HttpResponse userResponse = resilientHttpClient.execute(userRequest);
        if (userResponse.getStatusLine().getStatusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new GithubException(MessageFormat.format(Messages.USER_NOT_FOUND, username));
        }
        if (userResponse.getStatusLine().getStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
            throw new GithubException(MessageFormat.format(Messages.ACCESS_TOKEN_FOR_USER_HAS_EXPIRED, username));
        }
        GithubUserResponseModel githubUserResponseModel = parser.deserialize(UncheckedEntityUtils.getResponseBody(userResponse), GithubUserResponseModel.class);
        return ResponseEntity.ok(githubUserResponseModel);
    }

    @Override
    public ResponseEntity<GithubAccessTokenResponseModel> requestAccessTokenForUser(String username, String code) {
        UserServiceModel userServiceModel = getRequiredUser(username);
        HttpResponse accessTokenResponse = executeAccessTokenRequest(code);
        return processAccessTokenForUser(userServiceModel, accessTokenResponse);
    }

    private String getUsernameResource() {
        return environment.getGithubApiUrl() + "/" + com.code.to.learn.web.constants.Constants.USER_RESOURCE;
    }

    private UserServiceModel getRequiredUser(String username) {
        Optional<UserServiceModel> optionalUserServiceModel = userService.findByUsername(username);
        if (!optionalUserServiceModel.isPresent()) {
            throw new GithubException(MessageFormat.format(Messages.USER_WITH_THE_FOLLOWING_USERNAME_NOT_FOUND, username), Collections.emptyMap());
        }
        return optionalUserServiceModel.get();
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

    private ResponseEntity<GithubAccessTokenResponseModel> processAccessTokenForUser(UserServiceModel userServiceModel, HttpResponse accessTokenResponse) {
        GithubAccessTokenResponseModel githubAccessTokenResponseModel = parseGithubAccessTokenResponse(accessTokenResponse);
        setGithubAccessTokenForUser(userServiceModel, githubAccessTokenResponseModel);
        return ResponseEntity.ok(githubAccessTokenResponseModel);
    }

    private GithubAccessTokenResponseModel parseGithubAccessTokenResponse(HttpResponse accessTokenResponse) {
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

    private GithubAccessTokenResponseModel mapToGithubAccessToken(Map<String, String> githubAccessTokenQueryParameters) {
        return GithubAccessTokenResponseModel.fromAccessTokenQueryParameters(githubAccessTokenQueryParameters);
    }

    private void setGithubAccessTokenForUser(UserServiceModel userServiceModel, GithubAccessTokenResponseModel githubAccessTokenResponseModel) {
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = toInput(githubAccessTokenResponseModel);
        userServiceModel.setGithubAccessToken(githubAccessTokenServiceModel);
        userService.update(userServiceModel);
    }


    @Override
    protected Class<GithubAccessTokenServiceModel> getInputClass() {
        return GithubAccessTokenServiceModel.class;
    }

    @Override
    protected Class<GithubAccessTokenResponseModel> getOutputClass() {
        return GithubAccessTokenResponseModel.class;
    }
}