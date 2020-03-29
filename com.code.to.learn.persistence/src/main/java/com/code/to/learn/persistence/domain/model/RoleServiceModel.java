package com.code.to.learn.persistence.domain.model;

import java.util.List;

public class RoleServiceModel extends IdServiceModel {

    private String name;
    private List<UserServiceModel> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserServiceModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserServiceModel> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return name;
    }
}
