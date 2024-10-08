package com.angbit.angbit_advanced.common.constant;

public enum RoleEnum {
    ROLE_USER("ROLE_USER"),
    ROLE_ANONYMOUS("ROLE_ANONYMOUS"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }
}
