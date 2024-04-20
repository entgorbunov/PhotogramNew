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
public class Subscription {
    private Long id;
    private User follower;
    private User following;
    private LocalDateTime subscriptionTime;

}
