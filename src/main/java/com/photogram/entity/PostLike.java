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
public class PostLike {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime likeTime;
}
