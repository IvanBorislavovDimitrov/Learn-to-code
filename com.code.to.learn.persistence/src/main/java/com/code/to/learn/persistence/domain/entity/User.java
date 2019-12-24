package com.code.to.learn.persistence.domain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users", schema = "code")
public class User extends IdEntity implements UserDetails {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    public static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phoneNumber";

    @Column(length = 100, nullable = false, name = FIRST_NAME)
    private String firstName;

    @Column(length = 100, nullable = false, name = LAST_NAME)
    private String lastName;

    @Column(length = 100, nullable = false, name = USERNAME)
    private String username;

    @Column(length = 100, nullable = false, unique = true, name = PHONE_NUMBER)
    private String phoneNumber;

    @Column(length = 100, nullable = false, name = PASSWORD)
    private String password;

    @Column(length = 100, nullable = false, name = EMAIL)
    private String email;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "github_access_token_id", referencedColumnName = IdEntity.ID)
    private GithubAccessToken githubAccessToken;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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

    public GithubAccessToken getGithubAccessToken() {
        return githubAccessToken;
    }

    public void setGithubAccessToken(GithubAccessToken githubAccessToken) {
        this.githubAccessToken = githubAccessToken;
    }
}
