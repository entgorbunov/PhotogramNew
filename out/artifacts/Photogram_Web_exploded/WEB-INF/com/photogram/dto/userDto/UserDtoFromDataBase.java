package com.photogram.dto.userDto;

import com.photogram.entity.Gender;
import com.photogram.entity.Post;
import com.photogram.entity.Role;
import com.photogram.entity.Subscription;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserDtoFromDataBase {
    Long id;
    String username;
    String name;
    String profilePicture;
    String bio;
    String imageUrl;
    String birthday;
    Boolean isActive;
    String email;
    String password;
    Gender gender;
    Role role;
    List<Subscription> subscribers;
    List<Subscription> subscriptions;
    List<Post> posts;

}
