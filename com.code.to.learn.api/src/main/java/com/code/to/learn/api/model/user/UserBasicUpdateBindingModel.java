package com.code.to.learn.api.model.user;

import javax.validation.constraints.NotNull;

public class UserBasicUpdateBindingModel {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String birthDate;
    @NotNull
    private String description;

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
