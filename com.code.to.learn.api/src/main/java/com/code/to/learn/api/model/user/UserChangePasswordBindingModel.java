package com.code.to.learn.api.model.user;

import javax.validation.constraints.NotNull;

public class UserChangePasswordBindingModel {

    @NotNull
    private String token;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
