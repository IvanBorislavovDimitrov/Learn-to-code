package com.code.to.learn.web.api_facade;

import com.code.to.learn.api.api.github.GithubServiceApi;
import com.code.to.learn.api.model.github.GithubAccessTokenResponseModel;
import com.code.to.learn.api.model.github.GithubRepositoryResponseModel;
import com.code.to.learn.api.model.github.GithubRepositoryResponseModelExtended;
import com.code.to.learn.api.model.github.GithubUserResponseModel;
import com.code.to.learn.core.client.ResilientHttpClient;
import com.code.to.learn.core.client.UncheckedEntityUtils;
import com.code.to.learn.core.constant.Constants;
import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.domain.model.GithubAccessTokenServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.code.to.learn.persistence.exception.github.GithubException;
import com.code.to.learn.persistence.service.api.GithubService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.util.parser.Parser;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GithubServiceApiFacade extends ExtendableMapper<GithubAccessTokenServiceModel, GithubAccessTokenResponseModel> implements GithubServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubServiceApiFacade.class);

    private final Parser parser;
    private final ResilientHttpClient resilientHttpClient;
    private final ApplicationConfiguration applicationConfiguration;
    private final UserService userService;
    private final GithubService githubService;

    @Autowired
    public GithubServiceApiFacade(Parser parser, ResilientHttpClient resilientHttpClient,
                                  ModelMapper modelMapper, ApplicationConfiguration applicationConfiguration, UserService userService, GithubService githubService) {
        super(modelMapper);
        this.parser = parser;
        this.resilientHttpClient = resilientHttpClient;
        this.applicationConfiguration = applicationConfiguration;
        this.userService = userService;
        this.githubService = githubService;
    }

    @Override
    public ResponseEntity<GithubUserResponseModel> getGithubUserInfo(String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = getGithubAccessTokenServiceModel(username, userServiceModel);
        HttpGet userRequest = new HttpGet(getUsernameResource());
        userRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubAccessTokenServiceModel.getAccessToken());
        HttpResponse userResponse = resilientHttpClient.execute(userRequest);
        validateRequest(username, userResponse);
        GithubUserResponseModel githubUserResponseModel = parser.deserialize(UncheckedEntityUtils.getResponseBody(userResponse),
                GithubUserResponseModel.class);
        return ResponseEntity.ok(githubUserResponseModel);
    }

    private GithubAccessTokenServiceModel getGithubAccessTokenServiceModel(String username, UserServiceModel userServiceModel) {
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = userServiceModel.getGithubAccessToken();
        if (githubAccessTokenServiceModel == null) {
            throw new GithubException(MessageFormat.format(Messages.NO_GITHUB_ACCESS_TOKEN_FOR_USER, username));
        }
        return githubAccessTokenServiceModel;
    }

    private void validateRequest(String username, HttpResponse userResponse) {
        validateStatusNotFound(username, userResponse);
        validateStatusUnauthorized(username, userResponse);
    }

    private void validateStatusNotFound(String username, HttpResponse response) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new GithubException(MessageFormat.format(Messages.USER_NOT_FOUND, username));
        }
    }

    private void validateStatusUnauthorized(String username, HttpResponse response) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
            throw new GithubException(MessageFormat.format(Messages.ACCESS_TOKEN_FOR_USER_HAS_EXPIRED, username));
        }
    }

    @Override
    public ResponseEntity<GithubAccessTokenResponseModel> requestAccessTokenForUser(String username, String code) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        HttpResponse accessTokenResponse = executeAccessTokenRequest(code);
        return processAccessTokenForUser(userServiceModel, accessTokenResponse);
    }

    private String getUsernameResource() {
        return applicationConfiguration.getGithubApiUrl() + "/" + com.code.to.learn.web.constants.Constants.USER_RESOURCE;
    }

    private HttpResponse executeAccessTokenRequest(String code) {
        HttpPost accessTokenRequest = new HttpPost(applicationConfiguration.getGithubAccessTokenUrl());
        List<NameValuePair> parameters = getAccessTokenParameters(code);
        setFormEntity(accessTokenRequest, parameters);
        return resilientHttpClient.execute(accessTokenRequest);
    }

    private List<NameValuePair> getAccessTokenParameters(String code) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(Constants.CLIENT_ID_KEY, applicationConfiguration.getClientIdValue()));
        parameters.add(new BasicNameValuePair(Constants.CLIENT_SECRET_KEY, applicationConfiguration.getClientSecretValue()));
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
        updateGithubAccessTokenForUser(userServiceModel, githubAccessTokenResponseModel);
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
            throw new GithubException(com.code.to.learn.web.constants.Messages.INVALID_GITHUB_VERIFICATION_CODE, accessTokenQueryParameters);
        }
    }

    private GithubAccessTokenResponseModel mapToGithubAccessToken(Map<String, String> githubAccessTokenQueryParameters) {
        return GithubAccessTokenResponseModel.fromAccessTokenQueryParameters(githubAccessTokenQueryParameters);
    }

    private void updateGithubAccessTokenForUser(UserServiceModel userServiceModel, GithubAccessTokenResponseModel githubAccessTokenResponseModel) {
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = toInput(githubAccessTokenResponseModel);
        githubAccessTokenServiceModel.setUserServiceModel(userServiceModel);
        userServiceModel.setGithubAccessToken(githubAccessTokenServiceModel);
        githubService.save(githubAccessTokenServiceModel);
        GithubAccessTokenServiceModel updateEntity = githubService.findByAccessToken(githubAccessTokenResponseModel.getAccessToken());
        userServiceModel.setGithubAccessToken(updateEntity);
        userService.update(userServiceModel);
    }

    @Override
    public ResponseEntity<List<GithubRepositoryResponseModel>> getUserRepositories(String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        GithubAccessTokenServiceModel githubAccessToken = userServiceModel.getGithubAccessToken();
        HttpGet repositoriesRequest = new HttpGet(getRepositoryResource());
        repositoriesRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubAccessToken.getAccessToken());
        HttpResponse repositoriesResponse = resilientHttpClient.execute(repositoriesRequest);
        validateRequest(username, repositoriesResponse);
        String repositoriesResponseBody = UncheckedEntityUtils.getResponseBody(repositoriesResponse);
        return ResponseEntity.ok(parser.deserialize(repositoriesResponseBody, new TypeReference<List<GithubRepositoryResponseModel>>() {
        }));
    }

    @Override
    public ResponseEntity<GithubRepositoryResponseModelExtended> getRepositoryForUser(String username, String repositoryName) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        GithubAccessTokenServiceModel githubAccessTokenServiceModel = userServiceModel.getGithubAccessToken();
        GithubUserResponseModel githubUserInfo = getGithubUserInfo(username).getBody();
        HttpGet repositoryRequest = new HttpGet(getCertainRepositoryResource(githubUserInfo.getLogin(), repositoryName));
        repositoryRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubAccessTokenServiceModel.getAccessToken());
        HttpResponse repositoryResponse = resilientHttpClient.execute(repositoryRequest);
        validateRequest(username, repositoryResponse);
        String repositoryResponseBode = UncheckedEntityUtils.getResponseBody(repositoryResponse);
        return ResponseEntity.ok(parser.deserialize(repositoryResponseBode, new TypeReference<GithubRepositoryResponseModelExtended>() {
        }));
    }

    private String getRepositoryResource() {
        return applicationConfiguration.getGithubApiUrl() + com.code.to.learn.web.constants.Constants.REPOSITORIES_RESOURCE;
    }

    private String getCertainRepositoryResource(String username, String repositoryName) {
        return applicationConfiguration.getGithubApiUrl() + com.code.to.learn.web.constants.Constants.REPOSITORY_RESOURCE
                + "/" + username + "/" + repositoryName;
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
