package com.photogram.mapper;

import com.photogram.Exceptions.MapperException;
import com.photogram.dto.CreateUserDto;
import com.photogram.entity.Gender;
import com.photogram.entity.Role;
import com.photogram.entity.User;
import com.photogram.util.LocalDateFormatter;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CreateUserMapper implements Mapper<CreateUserDto, User> {
    public static final String IMAGE_FOLDER = "users/";

    @Getter
    public static final CreateUserMapper INSTANCE = new CreateUserMapper();

    @Override
    public User mapFrom(CreateUserDto userDto) {

       return User.builder()
                .username(userDto.getName())
               .image(IMAGE_FOLDER + userDto.getImage().getSubmittedFileName())
               .birthday(LocalDateFormatter.format(userDto.getBirthday()).atStartOfDay())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .gender(CreateUserMapper.getGender(userDto))
                .role(CreateUserMapper.getRole(userDto))
                .build();
    }

    private static Gender getGender(CreateUserDto userDto) {
        Optional<Gender> genderOptional = Gender.find(userDto.getGender());
        return genderOptional.orElseThrow(() -> new MapperException("Gender not found"));

    }
    private static Role getRole(CreateUserDto userDto) {
        Optional<Role> roleOptional = Role.find(userDto.getRole());
        return roleOptional.orElseThrow(() -> new MapperException("Role not found"));

    }

}
