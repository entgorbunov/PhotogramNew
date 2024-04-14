package com.photogram.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class CommentLike {
    private Long id;
    private Long userId;
    private Long commentId;
    private LocalDateTime likeTime;
}
