package com.code.to.learn.web.environment;

import com.code.to.learn.web.constants.Constants;

public class DynamicEnvironment implements Environment {

    @Override
    public String getGithubUrl() {
        return EnvironmentGetter.getVariable(Constants.GITHUB_URL);
    }
}
