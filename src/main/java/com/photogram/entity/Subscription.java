package com.photogram.entity;

import lombok.*;

import java.time.LocalDate;
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private Long userId;
    private Long followerUser;
    private LocalDate subscriptionDate;

}
