package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.UserApi;
import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.process.process.UserRegistrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserApiImpl implements UserApi {

    private final UserRegistrator userRegistrator;

    @Autowired
    public UserApiImpl(UserRegistrator userRegistrator) {
        this.userRegistrator = userRegistrator;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<?> sayHello() {
        return ResponseEntity.ok()
                .body("{\"message\":\"hello\"}");
    }

    @RequestMapping(value = "/users/register", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<?> register(@RequestBody @Valid UserBindingModel userBindingModel) {
        userRegistrator.register(userBindingModel);
        return ResponseEntity.ok()
                .body("{\"message\":\"done\"}");
    }
}
