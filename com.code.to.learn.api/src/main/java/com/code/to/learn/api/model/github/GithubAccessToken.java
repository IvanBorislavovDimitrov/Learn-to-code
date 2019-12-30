package com.code.to.learn.api.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GithubAccessToken {

    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    @JsonProperty("token_type")
    private String type;

    public static GithubAccessToken fromAccessTokenQueryParameters(Map<String, String> accessTokenQueryParameters) {
        GithubAccessToken githubAccessToken = new GithubAccessToken();
        githubAccessToken.setAccessToken(accessTokenQueryParameters.get("access_token"));
        githubAccessToken.setScope(accessTokenQueryParameters.get("scope"));
        githubAccessToken.setType(accessTokenQueryParameters.get("token_type"));
        return githubAccessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
