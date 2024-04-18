package com.photogram.builder;

import com.photogram.entity.User;

public class UserBuilder {
    public static User userBuilder(Long id, String username, String profilePicture, String bio, Boolean isPrivate,
                                   String imageUrl) {

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setProfilePicture(profilePicture);
        user.setBio(bio);
        user.setPrivate(isPrivate);
        user.setImageUrl(imageUrl);
        return user;
    }

}
