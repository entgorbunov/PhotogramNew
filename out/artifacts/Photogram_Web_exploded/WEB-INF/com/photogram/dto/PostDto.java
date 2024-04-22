package com.photogram.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class PostDto {
    Long id;
    Long userId;
    String caption;
    LocalDateTime postTime;
    String imageUrl;

}
