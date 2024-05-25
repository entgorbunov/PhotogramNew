package com.photogram.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {
    private LocalDate subscriptionDate;
    private Long userId;
    private Long followerId;
}
