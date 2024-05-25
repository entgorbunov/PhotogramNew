package com.photogram.entity;

import lombok.*;

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
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;
    private String text;

    public Post(Long id) {
        this.id = id;
    }
}
