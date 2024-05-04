package com.photogram.mapper;

import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.entity.User;
import lombok.Getter;

@Getter
public class UserMapperForDataBase  {

    @Getter
    public static final UserMapperForDataBase INSTANCE = new UserMapperForDataBase();


    public UserDtoFromDataBase toDto(User user) {
        return UserDtoFromDataBase.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .imageUrl(user.getImage())
                .birthday(user.getBirthday())
                .build();
    }






}
