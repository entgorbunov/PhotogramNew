package com.photogram.dto;

import lombok.Builder;
import lombok.Value;
@Builder
@Value
public class UserDto {
    Long id;
    String username;
    String bio;
}
