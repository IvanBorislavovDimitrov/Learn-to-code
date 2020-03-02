package com.code.to.learn.persistence.exception.github;

import java.util.Collections;
import java.util.Map;

public class GithubException extends RuntimeException {

    private final Map<String, String> accessTokenQueryParameters;

    public GithubException(String message) {
        this(message, Collections.emptyMap());
    }

    public GithubException(String message, Map<String, String> accessTokenQueryParameters) {
        super(message);
        this.accessTokenQueryParameters = accessTokenQueryParameters;
    }

    public Map<String, String> getAccessTokenQueryParameters() {
        return Collections.unmodifiableMap(accessTokenQueryParameters);
    }
}
