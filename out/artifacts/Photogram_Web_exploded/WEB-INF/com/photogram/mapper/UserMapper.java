package com.photogram.mapper;

import com.photogram.dto.UserDto;
import com.photogram.entity.User;

public class UserMapper {

    public static UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .bio(user.getBio())
                .build();
    }

    public static User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setBio(userDto.getBio());
        return user;
    }
}
