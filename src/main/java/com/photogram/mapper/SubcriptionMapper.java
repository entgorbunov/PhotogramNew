package com.photogram.mapper;

import com.photogram.dto.SubscriptionDto;
import com.photogram.entity.Subscription;

public class SubcriptionMapper implements Mapper<SubscriptionDto, Subscription> {

    UserMapper userMapper = new UserMapper();


    @Override
    public Subscription toEntity(SubscriptionDto dto) {
        return null;
    }

    @Override
    public SubscriptionDto toDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .subscriptionDate(subscription.getSubscriptionDate())
                .subscriberUser(userMapper.toDto(subscription.getSubscriberUser()))
                .subscriptionUser(userMapper.toDto(subscription.getSubscriptionUser()))
                .build();
    }


}
