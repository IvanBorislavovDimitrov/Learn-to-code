package com.code.to.learn.persistence.domain.entity;

import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends IdEntity {

    private static final String TYPE = "type";

    @Enumerated(EnumType.STRING)
    @Column(name = TYPE, nullable = false)
    private UserRole userRole;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
