package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubAccessTokenResponseModel;
import com.code.to.learn.api.model.github.GithubUserResponseModel;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/github", produces = MediaType.APPLICATION_JSON_VALUE)
public class GithubRestController {

    private final GithubServiceApi githubServiceApi;
    private final UsernameGetter usernameGetter;

    @Autowired
    public GithubRestController(GithubServiceApi githubServiceApi, UsernameGetter usernameGetter) {
        this.githubServiceApi = githubServiceApi;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(value = "/authorize")
    public ResponseEntity<GithubAccessTokenResponseModel> authorizeUser(@RequestParam String code) {
        return githubServiceApi.requestAccessTokenForUser(usernameGetter.getLoggedInUserUsername(), code);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<GithubUserResponseModel> githubUser() {
        return githubServiceApi.getGithubUserInfo(usernameGetter.getLoggedInUserUsername());
    }

}
