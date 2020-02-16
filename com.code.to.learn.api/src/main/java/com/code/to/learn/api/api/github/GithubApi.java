package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubAccessToken;
import com.code.to.learn.api.model.github.GithubUser;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/github")
public class GithubApi {

    private final GithubService githubService;
    private final UsernameGetter usernameGetter;

    @Autowired
    public GithubApi(GithubService githubService, UsernameGetter usernameGetter) {
        this.githubService = githubService;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(value = "/users/authorize")
    public ResponseEntity<GithubAccessToken> authorizeUser(@RequestParam String code) {
        return githubService.requestAccessTokenForUser(usernameGetter.getLoggedInUserUsername(), code);
    }

    @GetMapping(value = "/users/{username}")
    public ResponseEntity<GithubUser> githubUser(@PathVariable String username) {
        return githubService.getGithubUserInfo(username);
    }


}
