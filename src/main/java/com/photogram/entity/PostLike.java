package com.photogram.entity;


import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class PostLike {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime likeTime;
}
