package com.photogram.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class Subscription {
    private Long id;
    private Long followerId;
    private Long followingId;
    private LocalDateTime subscriptionTime;

}
