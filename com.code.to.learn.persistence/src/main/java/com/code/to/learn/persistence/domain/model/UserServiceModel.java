package com.code.to.learn.persistence.domain.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserServiceModel extends GenericServiceModel implements UserDetails {

    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String password;
    private String email;
    private GithubAccessTokenServiceModel githubAccessToken;
    private LocalDate birthDate;
    private List<CourseServiceModel> courses;
    private List<CourseServiceModel> coursesThatTeaches;
    private List<CourseServiceModel> coursesInCart;
    private List<RoleServiceModel> roles;
    private String profilePictureName;

    public UserServiceModel() {
        // Required by ModelMapper
    }

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

    public GithubAccessTokenServiceModel getGithubAccessToken() {
        return githubAccessToken;
    }

    public void setGithubAccessToken(GithubAccessTokenServiceModel githubAccessToken) {
        this.githubAccessToken = githubAccessToken;
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

    public List<CourseServiceModel> getCoursesThatTeaches() {
        return coursesThatTeaches;
    }

    public void setCoursesThatTeaches(List<CourseServiceModel> coursesThatTeaches) {
        this.coursesThatTeaches = coursesThatTeaches;
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

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                         .map(role -> new SimpleGrantedAuthority(role.getName()))
                         .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
