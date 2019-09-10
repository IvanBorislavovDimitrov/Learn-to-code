package com.code.to.learn.api;

import com.code.to.learn.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {

    private UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String sayHello() {
        userService.register();
        return "result";
    }
}
