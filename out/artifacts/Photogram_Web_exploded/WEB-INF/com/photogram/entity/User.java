package com.photogram.entity;

import lombok.*;

import java.time.LocalDateTime;

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
    private String profilePicture;
    private String bio;
    @Builder.Default
    private Boolean isPrivate = Boolean.FALSE;
    private String image;
    @Builder.Default
    private Boolean isActive = Boolean.TRUE;
    private String email;
    private String password;
    private Role role;
    private Gender gender;
    private LocalDateTime birthday;
    public User(Long id) {
        this.id = id;
    }
}
