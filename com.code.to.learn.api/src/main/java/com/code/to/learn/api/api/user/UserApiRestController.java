package com.code.to.learn.api.api.user;

import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserApiRestController {

    private final UserServiceApi userServiceApi;
    private final UsernameGetter usernameGetter;

    @Autowired
    public UserApiRestController(UserServiceApi userServiceApi, UsernameGetter usernameGetter) {
        this.userServiceApi = userServiceApi;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> register(@RequestBody @Valid UserBindingModel userBindingModel) {
        return userServiceApi.register(userBindingModel);
    }

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<Object> preflightLogin() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseModel>> getAllUsers() {
        return userServiceApi.findAllUsers();
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> getLoggedInUser() {
        String username = usernameGetter.getLoggedInUserUsername();
        return userServiceApi.findUser(username);
    }

}
