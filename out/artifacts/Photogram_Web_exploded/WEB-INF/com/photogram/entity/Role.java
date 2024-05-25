package com.photogram.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Role {
    ADMIN(1),
    USER(2),
    MODERATOR(3),
    GUEST(4);

    private final int roleId;

    Role(int roleId) {
        this.roleId = roleId;
    }

    public static Optional<Role> find(String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(role))
                .findFirst();
    }

    public static Optional<Role> findById(int id) {
        return Arrays.stream(values())
                .filter(role -> role.getRoleId() == id)
                .findFirst();
    }
}
