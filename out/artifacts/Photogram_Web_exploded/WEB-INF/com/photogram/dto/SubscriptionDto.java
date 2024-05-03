package com.photogram.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {
    private Long id;
    private LocalDateTime subscriptionDate;
    private UserDto subscriptionUser;
    private UserDto subscriberUser;
}
