package com.photogram.entity;

import lombok.*;
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
    private Boolean isPrivate;
    private String imageUrl;
    private Boolean isActive;

    public User(Long id) {
        this.id = id;
    }
}
