package com.photogram.dto.userDto;

import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDtoFromWeb {
    Long id;
    String name;
    String password;
    Part image;
    String email;
    String birthday;
    String role;
    String gender;
}
