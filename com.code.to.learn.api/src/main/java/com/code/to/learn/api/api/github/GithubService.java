package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubAccessToken;
import com.code.to.learn.api.model.github.GithubUser;
import org.springframework.http.ResponseEntity;

public interface GithubService {

    ResponseEntity<GithubUser> getGithubUserInfo(String username);

    ResponseEntity<GithubAccessToken> requestAccessTokenForUser(String loggedUserUsername, String code);
}
