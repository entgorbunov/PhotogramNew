package com.photogram.dto.postDto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class PostDtoFromDataBase {
    Long id;
    Long userId;
    String caption;
    LocalDateTime postTime;
    String imageUrl;
    String text;
    Boolean isDeleted;

}
