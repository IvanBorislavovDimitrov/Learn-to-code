package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.github.GithubService;
import com.code.to.learn.api.model.github.GithubAccessToken;
import com.code.to.learn.api.model.github.GithubUser;
import com.code.to.learn.api.parser.Parser;
import com.code.to.learn.api.parser.ParserFactory;
import com.code.to.learn.api.parser.ParserType;
import com.code.to.learn.core.constant.Messages;
import com.code.to.learn.core.exception.basic.LCException;
import com.code.to.learn.core.exception.basic.NotFoundException;
import com.code.to.learn.persistence.domain.model.GithubAccessTokenServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.web.client.ResilientHttpClient;
import com.code.to.learn.web.client.UncheckedEntityUtils;
import com.code.to.learn.web.constants.Constants;
import com.code.to.learn.web.environment.Environment;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service("githubServiceApiImpl")
public class GithubServiceImpl implements GithubService {

    private static final String USERS_RESOURCE = "/users";

    private final Parser parser = ParserFactory.createParser(ParserType.JSON);
    private final ResilientHttpClient resilientHttpClient;
    private final ModelMapper modelMapper;
    private final Environment environment;
    private final UserService userService;

    @Autowired
    public GithubServiceImpl(ResilientHttpClient resilientHttpClient, ModelMapper modelMapper, Environment environment, UserService userService) {
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
    public void requestAccessTokenForUser(String loggedUserUsername, String code) {
        HttpPost accessTokenRequest = new HttpPost(environment.getGithubAccessTokenUrl());
        List<NameValuePair> parameters = getAccessTokenParameters(code);
        setFormEntity(accessTokenRequest, parameters);
        HttpResponse accessTokenResponse = resilientHttpClient.execute(accessTokenRequest);
        GithubAccessToken githubAccessToken = parser.deserialize(UncheckedEntityUtils.getResponseBody(accessTokenResponse), GithubAccessToken.class);
        UserServiceModel user = userService.findByUsername(loggedUserUsername).orElseThrow(() -> new UsernameNotFoundException(loggedUserUsername));
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = modelMapper.map(githubAccessToken, GithubAccessTokenServiceModel.class);
        user.setGithubAccessTokenServiceModel(githubAccessTokenServiceModel);
        userService.update(user);
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
            throw new LCException(e);
        }
    }

    private String getUsernameResource(String username) {
        return environment.getGithubApiUrl() + "/" + USERS_RESOURCE + "/" + username;
    }
}
