package com.code.to.learn.persistence.domain.entity;

import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends GenericEntity<Role> {

    public static final String NAME = "name";

    @Enumerated(EnumType.STRING)
    @Column(name = NAME, nullable = false, unique = true)
    private UserRole name;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = User.class, cascade = {
            CascadeType.PERSIST,
    })
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users = new ArrayList<>();

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

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public Role merge(Role role) {
        setName(role.getName());
        setUsers(role.getUsers());
        return this;
    }
}
