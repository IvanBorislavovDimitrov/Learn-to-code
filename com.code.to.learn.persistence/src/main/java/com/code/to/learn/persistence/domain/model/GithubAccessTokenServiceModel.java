package com.code.to.learn.persistence.domain.model;

public class GithubAccessTokenServiceModel extends IdServiceModel {

    private String accessToken;
    private String scope;
    private String tokenType;
    private UserServiceModel userServiceModel;

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

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserServiceModel getUserServiceModel() {
        return userServiceModel;
    }

    public void setUserServiceModel(UserServiceModel userServiceModel) {
        this.userServiceModel = userServiceModel;
    }
}
