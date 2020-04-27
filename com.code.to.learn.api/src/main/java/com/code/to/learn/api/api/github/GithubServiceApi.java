package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubAccessTokenResponseModel;
import com.code.to.learn.api.model.github.GithubRepositoryResponseModel;
import com.code.to.learn.api.model.github.GithubUserResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GithubServiceApi {

    ResponseEntity<GithubUserResponseModel> getGithubUserInfo(String username);

    ResponseEntity<GithubAccessTokenResponseModel> requestAccessTokenForUser(String username, String code);

    ResponseEntity<List<GithubRepositoryResponseModel>> getUserRepositories(String username);
}
