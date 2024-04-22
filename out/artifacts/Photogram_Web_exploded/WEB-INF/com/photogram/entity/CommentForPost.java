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
public class CommentForPost {
    private Long id;
    private Post post;
    private User user;
    private String text;
    private LocalDateTime commentTime;
}
