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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    private final UserServiceApi userServiceApi;
    private final UsernameGetter usernameGetter;

    @Autowired
    public UserRestController(UserServiceApi userServiceApi, UsernameGetter usernameGetter) {
        this.userServiceApi = userServiceApi;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseModel> register(@Valid UserBindingModel userBindingModel) {
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

    @GetMapping(value = "/session")
    public ResponseEntity<Map<String, Object>> getUserAssociatedToSession() {
        String username = usernameGetter.getLoggedInUserUsername();
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        return ResponseEntity.ok(map);
    }

    @GetMapping(value = "/teachers")
    public ResponseEntity<List<UserResponseModel>> getTeachers() {
        return userServiceApi.findTeachers();
    }
}
