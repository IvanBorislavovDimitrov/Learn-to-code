package com.code.to.learn.core.environment;

import com.code.to.learn.core.constant.Constants;

import java.util.Objects;

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

    @Override
    public boolean useXMLParser() {
        return Objects.nonNull(EnvironmentGetter.getVariable(Constants.USE_XML_PARSER));
    }
}
