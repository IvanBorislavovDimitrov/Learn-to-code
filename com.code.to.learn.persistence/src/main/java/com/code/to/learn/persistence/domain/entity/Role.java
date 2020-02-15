package com.code.to.learn.persistence.domain.entity;

import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends IdEntity {

    public static final String NAME = "name";

    @Enumerated(EnumType.STRING)
    @Column(name = NAME, nullable = false)
    private UserRole name;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    public UserRole getName() {
        return name;
    }

    public void setName(UserRole name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
