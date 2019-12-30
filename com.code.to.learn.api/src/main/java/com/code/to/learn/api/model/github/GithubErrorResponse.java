package com.code.to.learn.api.model.github;

import java.util.Map;

public class GithubErrorResponse {

    private String error;
    private String errorDescription;
    private String errorUri;

    public static GithubErrorResponse fromAccessTokenQueryParameters(Map<String, String> accessTokenQueryParameters) {
        GithubErrorResponse githubErrorResponse = new GithubErrorResponse();
        githubErrorResponse.setError(accessTokenQueryParameters.get("error"));
        githubErrorResponse.setErrorDescription(accessTokenQueryParameters.get("error_description"));
        githubErrorResponse.setErrorUri(accessTokenQueryParameters.get("error_uri"));
        return githubErrorResponse;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorUri() {
        return errorUri;
    }

    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }
}
