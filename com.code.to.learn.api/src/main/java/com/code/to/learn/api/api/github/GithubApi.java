package com.code.to.learn.api.api.github;

import com.code.to.learn.api.api.BaseRestController;
import com.code.to.learn.api.model.github.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubApi extends BaseRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubApi.class);

    private final GithubService githubService;

    @Autowired
    public GithubApi(GithubService githubService) {
        this.githubService = githubService;
    }

    @PostMapping(value = "/github/users/authorize")
    public ResponseEntity<?> authorizeUser(@RequestParam String code) {
        return githubService.requestAccessTokenForUser(getLoggedUserUsername(), code);
    }

    @GetMapping(value = "/github/users/{username}")
    public ResponseEntity<GithubUser> githubUser(@PathVariable String username) {
        return githubService.getGithubUserInfo(username);
    }

    private String getLoggedUserUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return (String) principal;
    }

}
