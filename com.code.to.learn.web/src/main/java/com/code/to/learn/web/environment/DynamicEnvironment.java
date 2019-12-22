package com.code.to.learn.web.environment;

import com.code.to.learn.web.constants.Constants;

public class DynamicEnvironment implements Environment {

    @Override
    public String getGithubApiUrl() {
        return EnvironmentGetter.getVariable(Constants.GITHUB_URL);
    }

    @Override
    public String getGithubAccessTokenUrl() {
        return null;
    }

    @Override
    public String getClientIdValue() {
        return null;
    }

    @Override
    public String getClientSecretValue() {
        return null;
    }
}
