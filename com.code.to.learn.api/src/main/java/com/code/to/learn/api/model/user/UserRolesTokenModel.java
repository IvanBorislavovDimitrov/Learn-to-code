package com.code.to.learn.api.model.user;

import java.util.List;

public class UserRolesTokenModel {

    private final String roleAttribute;
    private final List<String> parsedRoles;

    public UserRolesTokenModel(String roleAttribute, List<String> parsedRoles) {
        this.roleAttribute = roleAttribute;
        this.parsedRoles = parsedRoles;
    }

    public String getRoleAttribute() {
        return roleAttribute;
    }

    public List<String> getParsedRoles() {
        return parsedRoles;
    }
}
