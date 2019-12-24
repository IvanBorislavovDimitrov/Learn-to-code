package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubUser;
import org.springframework.http.ResponseEntity;

public interface GithubService {

    ResponseEntity<GithubUser> getGithubUserInfo(String username);

    void requestAccessTokenForUser(String loggedUserUsername, String code);
}
