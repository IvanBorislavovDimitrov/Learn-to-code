package com.code.to.learn.persistence.domain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User extends GenericEntity<User> implements UserDetails {

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phoneNumber";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "password";
    private static final String PROFILE_PICTURE_NAME = "profile_picture_name";

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

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = PROFILE_PICTURE_NAME)
    private String profilePictureName;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "github_access_token_id", referencedColumnName = GenericEntity.ID)
    private GithubAccessToken githubAccessToken;

    @ManyToMany(mappedBy = "attendants", fetch = FetchType.LAZY, targetEntity = Course.class)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(targetEntity = Course.class, fetch = FetchType.LAZY, mappedBy = "teacher")
    private List<Course> coursesThatTeaches = new ArrayList<>();

    @ManyToMany(mappedBy = "futureAttendants", targetEntity = Course.class, fetch = FetchType.LAZY)
    private List<Course> coursesInCart = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

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

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
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
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCoursesThatTeaches() {
        return coursesThatTeaches;
    }

    public void setCoursesThatTeaches(List<Course> coursesThatTeaches) {
        this.coursesThatTeaches = coursesThatTeaches;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Course> getCoursesInCart() {
        return coursesInCart;
    }

    public void setCoursesInCart(List<Course> coursesInCart) {
        this.coursesInCart = coursesInCart;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public User merge(User user) {
        setUsername(user.getUsername());
        setPhoneNumber(user.getPhoneNumber());
        setPassword(user.getPassword());
        setEmail(user.getEmail());
        setBirthDate(user.getBirthDate());
        setGithubAccessToken(user.getGithubAccessToken());
        setCourses(user.getCourses());
        setCoursesThatTeaches(user.getCoursesThatTeaches());
        setCoursesInCart(user.getCoursesInCart());
        setRoles(user.getRoles());
        return this;
    }
}
