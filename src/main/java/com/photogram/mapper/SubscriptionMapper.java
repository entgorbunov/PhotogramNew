package com.photogram.mapper;

import com.photogram.dto.SubscriptionDto;
import com.photogram.entity.Subscription;
import com.photogram.mapper.userMapper.UserMapperForDataBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper implements Mapper<Subscription, SubscriptionDto> {

    private static final UserMapperForDataBase USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE = UserMapperForDataBase.getUserMapperForDataBase();

    private static final AtomicReference<SubscriptionMapper> SUBSCRIPTION_MAPPER_ATOMIC_REFERENCE = new AtomicReference<>();

    public static SubscriptionMapper getSubscriptionMapper() {
        if (SUBSCRIPTION_MAPPER_ATOMIC_REFERENCE.get() == null) {
            SubscriptionMapper subscriptionMapper = new SubscriptionMapper();
            if (SUBSCRIPTION_MAPPER_ATOMIC_REFERENCE.compareAndSet(null, subscriptionMapper)) {
                return subscriptionMapper;
            } else {
                return SUBSCRIPTION_MAPPER_ATOMIC_REFERENCE.get();
            }
        }
        return SUBSCRIPTION_MAPPER_ATOMIC_REFERENCE.get();
    }

    @Override
    public SubscriptionDto toDto(Subscription entity) {
        return SubscriptionDto.builder()
                .subscriptionDate(entity.getSubscriptionDate())
                .followerId(entity.getFollowerUser())
                .userId(entity.getUserId())
                .build();
    }

    @Override
    public Subscription toEntity(SubscriptionDto subscriptionDto) {
        return Subscription.builder()
                .subscriptionDate(subscriptionDto.getSubscriptionDate())
                .userId(subscriptionDto.getUserId())
                .followerUser(subscriptionDto.getFollowerId())
                .build();
    }

}
