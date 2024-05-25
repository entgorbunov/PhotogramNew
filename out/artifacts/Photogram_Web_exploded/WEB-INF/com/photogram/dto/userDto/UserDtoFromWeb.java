package com.photogram.dto.userDto;

import com.photogram.entity.Gender;
import com.photogram.entity.Role;
import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDtoFromWeb {
    Long id;
    String username;
    String name;
    String email;
    String profilePicture;
    String bio;
    Boolean isPrivate;
    Part image;
    Boolean isActive;
    String password;
    Role role;
    Gender gender;
    LocalDate birthday;
}
