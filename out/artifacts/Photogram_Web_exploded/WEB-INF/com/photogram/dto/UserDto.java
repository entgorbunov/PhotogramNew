package com.photogram.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    Long id;
    String name;
    String password;
    String imagePath;
    String email;
    String birthday;
    String role;
    String gender;
}
