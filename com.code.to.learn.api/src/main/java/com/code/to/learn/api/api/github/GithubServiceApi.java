package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubAccessToken;
import com.code.to.learn.api.model.github.GithubUserResponseModel;
import org.springframework.http.ResponseEntity;

public interface GithubServiceApi {

    ResponseEntity<GithubUserResponseModel> getGithubUserInfo(String username);

    ResponseEntity<GithubAccessToken> requestAccessTokenForUser(String loggedUserUsername, String code);
}
