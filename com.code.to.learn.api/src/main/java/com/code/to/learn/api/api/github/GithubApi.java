package com.code.to.learn.api.api.github;

import com.code.to.learn.api.api.BaseRestController;
import com.code.to.learn.api.model.github.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubApi extends BaseRestController {

    private final GithubService githubService;

    @Autowired
    public GithubApi(GithubService githubService) {
        this.githubService = githubService;
    }

    @PostMapping(value = "/github/users/authorize")
    public ResponseEntity<String> authorizeUser(@RequestParam String code) {
        String token = githubService.getAccessTokenForUser(code);
        System.out.println("TOKEN: " + token);
        return ResponseEntity.ok(token);
    }

    @GetMapping(value = "/github/users/{username}")
    public ResponseEntity<GithubUser> githubUser(@PathVariable String username) {
        return githubService.getGithubUserInfo(username);
    }
}
