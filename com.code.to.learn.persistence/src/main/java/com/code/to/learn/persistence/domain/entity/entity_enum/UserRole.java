package com.code.to.learn.persistence.domain.entity.entity_enum;

public enum UserRole {
    ROLE_ADMIN("ADMIN"), ROLE_MODERATOR("MODERATOR"), ROLE_USER("USER");

    String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
