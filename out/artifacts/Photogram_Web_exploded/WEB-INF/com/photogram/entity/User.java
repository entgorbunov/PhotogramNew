package com.photogram.entity;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

public class User {

    private Long id;
    private String username;
    private String name;
    private String profilePicture;
    private String bio;
    private String image;
    @Builder.Default
    private Boolean isActive = Boolean.TRUE;
    private String email;
    private String password;
    @Builder.Default
    private Role role = Role.USER;
    private Gender gender;
    private LocalDate birthday;
    public User(Long id) {
        this.id = id;
    }
}
