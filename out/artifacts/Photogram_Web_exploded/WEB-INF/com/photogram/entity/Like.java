package com.photogram.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private Long id;
    private Long userId;
    private Long postId;
    private Long commentId;
    private LocalDateTime likeTime;

    public Boolean isPostLike() {
        return commentId == null;
    }

    public Boolean isCommentLike() {
        return commentId != null;
    }
}
