package com.photogram.mapper;

import com.photogram.dto.UserDto;
import com.photogram.entity.Gender;
import com.photogram.entity.Role;
import com.photogram.entity.User;
import com.photogram.exceptions.MapperException;
import com.photogram.util.LocalDateTimeFormatter;
import lombok.Getter;

import java.util.Optional;

@Getter
public class UserMapper implements Mapper<UserDto, User> {
    public static final String IMAGE_FOLDER = "users/";

    @Getter
    public static final UserMapper INSTANCE = new UserMapper();

    @Override
    public User toEntity(UserDto userDto) {
       return User.builder()
                .username(userDto.getName())
               .image(IMAGE_FOLDER + userDto.getImagePath())
               .birthday(LocalDateTimeFormatter.format(userDto.getBirthday()))
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .gender(UserMapper.getGender(userDto))
                .role(UserMapper.getRole(userDto))
                .build();
    }
    @Override
    public UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getUsername())
                .birthday(user.getBirthday().toString())
                .email(user.getEmail())
                .imagePath(user.getImage())
                .role(user.getRole().name())
                .gender(user.getGender().name())
                .build();
    }

    private static Gender getGender(UserDto userDto) {
        Optional<Gender> genderOptional = Gender.find(userDto.getGender());
        return genderOptional.orElseThrow(() -> new MapperException("Gender not found"));

    }

    private static Role getRole(UserDto userDto) {
        Optional<Role> roleOptional = Role.find(userDto.getRole());
        return roleOptional.orElseThrow(() -> new MapperException("Role not found"));

    }

}
