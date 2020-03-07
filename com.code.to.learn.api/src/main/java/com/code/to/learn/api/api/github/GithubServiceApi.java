package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubAccessTokenResponseModel;
import com.code.to.learn.api.model.github.GithubUserResponseModel;
import org.springframework.http.ResponseEntity;

public interface GithubServiceApi {

    ResponseEntity<GithubUserResponseModel> getGithubUserInfo(String username);

    ResponseEntity<GithubAccessTokenResponseModel> requestAccessTokenForUser(String username, String code);
}
