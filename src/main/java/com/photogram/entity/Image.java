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
public class Image {
    private Long id;
    private String path;
    private Post postId;
    private User userId;
    private Boolean isDeleted;
    private LocalDateTime uploadedTime;

}
