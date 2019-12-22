package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.github.GithubService;
import com.code.to.learn.api.model.github.GithubUser;
import com.code.to.learn.api.parser.Parser;
import com.code.to.learn.api.parser.ParserFactory;
import com.code.to.learn.api.parser.ParserType;
import com.code.to.learn.web.client.ResilientHttpClient;
import com.code.to.learn.web.client.UncheckedEntityUtils;
import com.code.to.learn.web.environment.Environment;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("githubServiceApiImpl")
public class GithubServiceImpl implements GithubService {

    private static final String USERS_RESOURCE = "/users";

    private final Parser parser = ParserFactory.createParser(ParserType.JSON);
    private final ResilientHttpClient resilientHttpClient;
    private final Environment environment;

    @Autowired
    public GithubServiceImpl(ResilientHttpClient resilientHttpClient, Environment environment) {
        this.resilientHttpClient = resilientHttpClient;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<GithubUser> getGithubUserInfo(String username) {
        HttpGet httpGet = new HttpGet(getUsernameResource(username));
        HttpResponse userResponse = resilientHttpClient.execute(httpGet);
        GithubUser githubUser = parser.deserialize(UncheckedEntityUtils.getResponseBody(userResponse), GithubUser.class);
        return ResponseEntity.ok(githubUser);
    }

    private String getUsernameResource(String username) {
        return environment.getGithubUrl() + "/" + USERS_RESOURCE + "/" + username;
    }
}
