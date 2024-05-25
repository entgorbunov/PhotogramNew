package com.photogram.mapper.userMapper;

import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Gender;
import com.photogram.entity.Role;
import com.photogram.entity.User;
import com.photogram.exceptions.MapperException;
import com.photogram.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapperForWeb implements Mapper<User, UserDtoFromWeb> {
    public static final String IMAGE_FOLDER = "/Users/ent/Desktop/Pictures/users/";

    private static final AtomicReference<UserMapperForWeb> USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE = new AtomicReference<>();

    public static UserMapperForWeb getUserMapperForWebAtomicReference() {
        USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get();
        if (USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get() == null) {
            UserMapperForWeb userMapper = new UserMapperForWeb();
            if (USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE.compareAndSet(null, userMapper)) {
                return userMapper;
            } else {
                return USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get();
            }
        }
        return USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get();
    }


    public User toEntity(UserDtoFromWeb userDtoFromWeb) {
        return User.builder()
                .username(userDtoFromWeb.getUsername())
                .name(userDtoFromWeb.getName())
                .image(IMAGE_FOLDER + userDtoFromWeb.getImage().getSubmittedFileName())
                .birthday(LocalDate.from(userDtoFromWeb.getBirthday()))
                .email(userDtoFromWeb.getEmail())
                .password(userDtoFromWeb.getPassword())
                .gender(UserMapperForWeb.getGender(userDtoFromWeb))
                .profilePicture(userDtoFromWeb.getProfilePicture())
                .bio(userDtoFromWeb.getBio())
                .isActive(userDtoFromWeb.getIsActive())
                .build();
    }

    public UserDtoFromWeb toDto(User user) {
        return UserDtoFromWeb.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .isActive(user.getIsActive())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .password(user.getPassword())
                .gender(user.getGender())
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .build();
    }


    private static Gender getGender(UserDtoFromWeb userDtoFromWeb) {
        Optional<Gender> genderOptional = Gender.find(String.valueOf(userDtoFromWeb.getGender()));
        return genderOptional.orElseThrow(() -> new MapperException("Gender not found"));

    }

    private static Role getRole(UserDtoFromWeb userDtoFromWeb) {
        Optional<Role> roleOptional = Role.find(String.valueOf(userDtoFromWeb.getRole()));
        return roleOptional.orElseThrow(() -> new MapperException("Role not found"));

    }

}
