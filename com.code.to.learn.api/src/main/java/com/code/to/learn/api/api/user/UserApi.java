package com.code.to.learn.api.api.user;

import com.code.to.learn.api.api.BaseRestController;
import com.code.to.learn.api.model.user.UserBindingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public class UserApi extends BaseRestController {

    private final UserServiceApi userServiceApi;

    @Autowired
    public UserApi(UserServiceApi userServiceApi) {
        this.userServiceApi = userServiceApi;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sayHello() {
        return userServiceApi.sayHello();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/register",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid UserBindingModel userBindingModel) {
        return userServiceApi.register(userBindingModel);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers() {
        return userServiceApi.getAllUsers();
    }

}
