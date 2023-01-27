package com.aptech.diplom.models;

public enum UserRoles {
    USER("USER"),
    SUPERUSER("SUPERUSER");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRoleName() {
        return role;
    }
}
