package com.code.to.learn.api.api.user;

import com.code.to.learn.api.authentication.Authenticator;
import com.code.to.learn.api.model.authentication.AuthenticationRequest;
import com.code.to.learn.api.model.authentication.JwtTokenResponse;
import com.code.to.learn.api.model.user.*;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    private final UserServiceApi userServiceApi;
    private final UsernameGetter usernameGetter;
    private final Authenticator authenticator;

    @Autowired
    public UserRestController(UserServiceApi userServiceApi, UsernameGetter usernameGetter, Authenticator authenticator) {
        this.userServiceApi = userServiceApi;
        this.usernameGetter = usernameGetter;
        this.authenticator = authenticator;
    }

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponseModel> register(@Valid UserBindingModel userBindingModel) {
        return userServiceApi.register(userBindingModel);
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        authenticator.authenticate(usernamePasswordAuthenticationToken);
        return userServiceApi.generateTokenForUser(authenticationRequest.getUsername());
    }

    @GetMapping
    public ResponseEntity<List<UserResponseModel>> getAllUsers() {
        return userServiceApi.findAllUsers();
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String username) {
        return userServiceApi.findUser(username);
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

    @PostMapping(value = "/activate/{username}")
    public ResponseEntity<UserResponseModel> activateAccount(@PathVariable String username) {
        return userServiceApi.activateAccount(username);
    }

    @PostMapping(value = "/forgotten-password/{username}")
    public ResponseEntity<?> requestPasswordChange(@PathVariable String username) {
        return userServiceApi.sendEmailForPasswordReset(username);
    }

    @PatchMapping(value = "/change-forgotten-password")
    public ResponseEntity<UserResponseModel> changeForgottenPassword(@RequestBody @Valid UserChangePasswordBindingModel
                                                                             userChangePasswordBindingModel) {
        return userServiceApi.changeForgottenPassword(userChangePasswordBindingModel);
    }

    @PostMapping(value = "/change-profile-picture/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseModel> changeProfilePicture(@PathVariable String username, @RequestParam MultipartFile profilePicture) {
        if (profilePicture != null && profilePicture.getSize() > 20971520) {
            throw new IllegalArgumentException("File too big");
        }
        return userServiceApi.updateProfilePicture(username, profilePicture);
    }

    @PatchMapping(value = "/update/basic/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> updateProfileBasicInformation(@PathVariable String username,
                                                                           @RequestBody UserBasicUpdateBindingModel userBasicUpdateBindingModel) {
        return userServiceApi.updateBasicProfileInformation(username, userBasicUpdateBindingModel);
    }

    @PostMapping(value = "/update/deactivate/{username}")
    public ResponseEntity<UserResponseModel> deactivateUserProfile(@PathVariable String username) {
        return userServiceApi.deactivateProfile(username);
    }

    @PostMapping(value = "/update/deactivate")
    public ResponseEntity<UserResponseModel> deactivateCurrentUser() {
        String loggedUserUsername = usernameGetter.getLoggedInUserUsername();
        return userServiceApi.deactivateProfile(loggedUserUsername);
    }

    @PatchMapping(value = "/update/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeUserPassword(@RequestBody @Valid UserPasswordChangeBindingModel userPasswordChangeBindingModel) {
        String loggedUserUsername = usernameGetter.getLoggedInUserUsername();
        userPasswordChangeBindingModel.setUsername(loggedUserUsername);
        return userServiceApi.changeUserPassword(userPasswordChangeBindingModel);
    }

}
