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
public class Subscription {
    private Long id;
    private Long followerId;
    private Long followingId;
    private LocalDateTime subscriptionTime;

}
