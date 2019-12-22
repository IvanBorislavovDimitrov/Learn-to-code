package com.code.to.learn.web.environment;

import com.code.to.learn.web.constants.Constants;

public class StaticEnvironment implements Environment {

    @Override
    public String getGithubUrl() {
        return Constants.DEFAULT_GITHUB_URL;
    }
}
