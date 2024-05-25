package com.photogram.dto.postDto;

import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
@Value
@Builder
public class PostDtoFromWeb {
    Long id;
    Long userId;
    String caption;
    LocalDateTime postTime;
    Part image;
    String text;
    Boolean isDeleted;
}
