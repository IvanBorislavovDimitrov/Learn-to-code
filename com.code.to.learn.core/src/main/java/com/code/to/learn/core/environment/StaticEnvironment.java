package com.code.to.learn.core.environment;

import com.code.to.learn.core.constant.Constants;

public class StaticEnvironment implements Environment {

    @Override
    public String getGithubApiUrl() {
        return Constants.DEFAULT_GITHUB_URL;
    }

    @Override
    public String getGithubAccessTokenUrl() {
        return Constants.GITHUB_ACCESS_TOKEN_URL;
    }

    @Override
    public String getClientIdValue() {
        return Constants.CLIENT_ID_VALUE;
    }

    @Override
    public String getClientSecretValue() {
        return Constants.CLIENT_SECRET_VALUE;
    }

    @Override
    public boolean useXMLParser() {
        return false;
    }
}
