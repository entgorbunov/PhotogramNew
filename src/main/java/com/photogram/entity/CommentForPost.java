package com.photogram.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentForPost {
    private Long id;
    private Post post;
    private User user;
    private String text;
    private LocalDateTime commentTime;
}
