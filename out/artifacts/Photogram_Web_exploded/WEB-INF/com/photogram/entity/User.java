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
    private boolean isPrivate;
    private String imageUrl;

    public User(Long id) {
        this.id = id;
    }
}
