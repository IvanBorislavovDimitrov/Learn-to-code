package com.code.to.learn.persistence.domain.model;

import java.time.LocalDate;
import java.util.List;

public class UserServiceModel extends IdServiceModel {

    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String password;
    private String email;
    private GithubAccessTokenServiceModel githubAccessTokenServiceModel;
    private LocalDate birthDate;
    private List<CourseServiceModel> courses;
    private List<CourseServiceModel> courseTaught;
    private List<CourseServiceModel> coursesInCart;
    private List<RoleServiceModel> roles;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GithubAccessTokenServiceModel getGithubAccessTokenServiceModel() {
        return githubAccessTokenServiceModel;
    }

    public void setGithubAccessTokenServiceModel(GithubAccessTokenServiceModel githubAccessTokenServiceModel) {
        this.githubAccessTokenServiceModel = githubAccessTokenServiceModel;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<CourseServiceModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseServiceModel> courses) {
        this.courses = courses;
    }

    public List<CourseServiceModel> getCourseTaught() {
        return courseTaught;
    }

    public void setCourseTaught(List<CourseServiceModel> courseTaught) {
        this.courseTaught = courseTaught;
    }

    public List<CourseServiceModel> getCoursesInCart() {
        return coursesInCart;
    }

    public void setCoursesInCart(List<CourseServiceModel> coursesInCart) {
        this.coursesInCart = coursesInCart;
    }

    public List<RoleServiceModel> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleServiceModel> roles) {
        this.roles = roles;
    }
}
