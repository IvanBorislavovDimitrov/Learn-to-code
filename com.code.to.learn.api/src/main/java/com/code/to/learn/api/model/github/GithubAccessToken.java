package com.code.to.learn.api.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubAccessToken {

    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    private String type;

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
