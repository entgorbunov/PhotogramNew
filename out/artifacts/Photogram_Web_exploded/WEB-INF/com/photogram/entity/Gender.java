package com.photogram.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Gender {
    MALE(1),
    FEMALE(2);

    private final int genderId;

    Gender(int genderId) {
        this.genderId = genderId;
    }

    public static Optional<Gender> findByGenderId(int id) {
        return Arrays.stream(values())
                .filter(gender -> gender.getGenderId() == id)
                .findFirst();
    }

    public static Optional<Gender> find(String name) {
        return Arrays.stream(values())
                .filter(gender -> gender.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
