package com.code.to.learn.api.api.github;

import com.code.to.learn.api.api.BaseRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class GithubApi extends BaseRestController {

    @RequestMapping(value = "/github", method = RequestMethod.GET)
    public String getHome() {
        return null;
    }
}
