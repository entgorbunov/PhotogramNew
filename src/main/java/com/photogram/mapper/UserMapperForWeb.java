package com.photogram.mapper;

import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Gender;
import com.photogram.entity.Role;
import com.photogram.entity.User;
import com.photogram.exceptions.MapperException;
import com.photogram.util.LocalDateTimeFormatter;
import lombok.Getter;

import java.util.Optional;

@Getter
public class UserMapperForWeb  {
    public static final String IMAGE_FOLDER = "/Users/ent/Desktop/Pictures/users/";

    @Getter
    public static final UserMapperForWeb INSTANCE = new UserMapperForWeb();

    public User toEntity(UserDtoFromWeb userDtoFromWeb) {
       return User.builder()
                .username(userDtoFromWeb.getName())
               .image(IMAGE_FOLDER + userDtoFromWeb.getImage().getSubmittedFileName())
               .birthday(LocalDateTimeFormatter.format(userDtoFromWeb.getBirthday()))
                .email(userDtoFromWeb.getEmail())
                .password(userDtoFromWeb.getPassword())
                .gender(UserMapperForWeb.getGender(userDtoFromWeb))
                .role(UserMapperForWeb.getRole(userDtoFromWeb))
                .build();
    }


    private static Gender getGender(UserDtoFromWeb userDtoFromWeb) {
        Optional<Gender> genderOptional = Gender.find(userDtoFromWeb.getGender());
        return genderOptional.orElseThrow(() -> new MapperException("Gender not found"));

    }

    private static Role getRole(UserDtoFromWeb userDtoFromWeb) {
        Optional<Role> roleOptional = Role.find(userDtoFromWeb.getRole());
        return roleOptional.orElseThrow(() -> new MapperException("Role not found"));

    }

}
