package com.photogram.entity;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {
    private Long id;
    private Long userId;
    private Long commentId;
    private LocalDateTime likeTime;
}
