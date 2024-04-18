package com.photogram.entity;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private User user;
    private String caption;
    private LocalDateTime postTime;
    private String imageUrl;

    public Post(Long id) {
        this.id = id;
    }
}
