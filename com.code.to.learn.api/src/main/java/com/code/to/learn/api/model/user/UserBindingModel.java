package com.code.to.learn.api.model.user;

import com.code.to.learn.api.constant.Constants;
import com.code.to.learn.api.validator.annotation.EmailConstraint;
import com.code.to.learn.api.validator.annotation.NotBefore;
import com.code.to.learn.api.validator.annotation.PasswordConstraint;
import com.code.to.learn.api.validator.annotation.PhoneNumberConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class UserBindingModel {

    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @PhoneNumberConstraint
    private String phoneNumber;

    @NotNull
    @PasswordConstraint
    private String password;

    @NotNull
    @PasswordConstraint
    private String confirmPassword;

    @NotNull
    @EmailConstraint
    private String email;

    @NotNull
    @NotBefore(date = Constants.NOT_BEFORE_DATE)
    private LocalDate localDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
