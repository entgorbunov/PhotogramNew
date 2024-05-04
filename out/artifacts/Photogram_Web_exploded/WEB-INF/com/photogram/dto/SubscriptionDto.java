package com.photogram.dto;

import com.photogram.dto.userDto.UserDtoFromDataBase;
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
    private UserDtoFromDataBase subscriptionUser;
    private UserDtoFromDataBase subscriberUser;
}
