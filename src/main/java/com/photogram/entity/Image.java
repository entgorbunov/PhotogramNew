package com.photogram.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private Long id;
    private String path;
    private Post postId;
    private User userId;
    private LocalDateTime uploadedTime;

}
