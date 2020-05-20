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
public class User extends IdEntity<User> implements UserDetails {

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String RESET_PASSWORD_TOKEN = "resetPasswordToken";

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false, name = USERNAME)
    private String username;

    @Column(length = 100, nullable = false, unique = true, name = PHONE_NUMBER)
    private String phoneNumber;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false, name = EMAIL)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Basic
    private String profilePictureName;

    @Lob
    private String description;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "github_access_token_id", referencedColumnName = IdEntity.ID)
    private GithubAccessToken githubAccessToken;

    @ManyToMany(mappedBy = "attendants", fetch = FetchType.LAZY, targetEntity = Course.class, cascade = CascadeType.MERGE)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(targetEntity = Course.class, fetch = FetchType.LAZY, mappedBy = "teacher", cascade = CascadeType.MERGE)
    private List<Course> coursesThatTeaches = new ArrayList<>();

    @ManyToMany(mappedBy = "futureAttendants", targetEntity = Course.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Course> coursesInCart = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "author", targetEntity = Comment.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Comment> comments = new ArrayList<>();

    @Column()
    private boolean isEnabled;

    private String resetPasswordToken;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = ID))
    private List<LoginRecord> loginRecords;

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
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void emptyCart() {
        this.coursesInCart.clear();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public List<LoginRecord> getLoginRecords() {
        return loginRecords;
    }

    public void setLoginRecords(List<LoginRecord> loginRecords) {
        this.loginRecords = loginRecords;
    }

    @Override
    public User merge(User user) {
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
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
        setProfilePictureName(user.getProfilePictureName());
        setDescription(user.getDescription());
        setComments(user.getComments());
        setEnabled(user.isEnabled());
        setResetPasswordToken(user.getResetPasswordToken());
        setLoginRecords(user.getLoginRecords());
        return this;
    }

    @Embeddable
    public static class LoginRecord implements Comparable<LoginRecord> {
        @Basic
        private LocalDate date;
        @Basic
        private String additionalInformation;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getAdditionalInformation() {
            return additionalInformation;
        }

        public void setAdditionalInformation(String additionalInformation) {
            this.additionalInformation = additionalInformation;
        }

        @Override
        public int compareTo(LoginRecord loginRecord) {
            return loginRecord.getDate().compareTo(date);
        }
    }
}
