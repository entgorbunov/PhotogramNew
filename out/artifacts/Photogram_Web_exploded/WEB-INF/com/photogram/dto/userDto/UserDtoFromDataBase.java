package com.photogram.dto.userDto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UserDtoFromDataBase {
    Long id;
    String username;
    String profilePicture;
    String bio;
    String imageUrl;
    LocalDateTime birthday;

}
