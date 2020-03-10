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
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApiRestController {

    private final UserServiceApi userServiceApi;
    private final UsernameGetter usernameGetter;

    @Autowired
    public UserApiRestController(UserServiceApi userServiceApi, UsernameGetter usernameGetter) {
        this.userServiceApi = userServiceApi;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(value = "/register")
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

    @GetMapping
    public ResponseEntity<List<UserResponseModel>> getAllUsers() {
        return userServiceApi.findAllUsers();
    }

    @GetMapping(value = "/user")
    public ResponseEntity<UserResponseModel> getLoggedInUser() {
        String username = usernameGetter.getLoggedInUserUsername();
        return userServiceApi.findUser(username);
    }

    @GetMapping(value = "/filter/username")
    public ResponseEntity<List<UserResponseModel>> getUsersContaining(@RequestParam String username) {
        return userServiceApi.findUsersByUsernameContaining(username);
    }

    @PutMapping(value = "change-roles/{username}")
    public ResponseEntity<UserResponseModel> changeUserRoles(@PathVariable String username, @RequestParam List<String> roles) {
        return userServiceApi.changeUserRoles(username, roles);
    }

}
