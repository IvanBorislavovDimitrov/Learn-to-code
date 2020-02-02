package com.code.to.learn.api.api.github;

import com.code.to.learn.api.model.github.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/github")
public class GithubApi {

    private final GithubService githubService;

    @Autowired
    public GithubApi(GithubService githubService) {
        this.githubService = githubService;
    }

    @PostMapping(value = "/users/authorize")
    public ResponseEntity<?> authorizeUser(@RequestParam String code) {
        return githubService.requestAccessTokenForUser(getLoggedUserUsername(), code);
    }

    @GetMapping(value = "/users/{username}")
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
