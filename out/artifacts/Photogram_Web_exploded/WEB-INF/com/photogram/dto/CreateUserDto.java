package com.photogram.dto;

import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserDto {
    String name;
    String password;
    Part image;
    String email;
    String birthday;
    String role;
    String gender;
}
