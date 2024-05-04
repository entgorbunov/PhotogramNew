//package com.photogram.mapper;
//
//import com.photogram.dto.SubscriptionDto;
//import com.photogram.entity.Subscription;
//
//public class SubcriptionMapper implements Mapper<SubscriptionDto, Subscription> {
//
//    UserMapperForDataBase USER_MAPPER_FOR_DATA_BASE = UserMapperForDataBase.getINSTANCE();
//
//
//    @Override
//    public Subscription toEntity(SubscriptionDto dto) {
//        return null;
//    }
//
//    @Override
//    public SubscriptionDto toDto(Subscription subscription) {
//        return SubscriptionDto.builder()
//                .id(subscription.getId())
//                .subscriptionDate(subscription.getSubscriptionDate())
//                .subscriberUser(userMapperForWeb.toDto(subscription.getSubscriberUser()))
//                .subscriptionUser(userMapperForWeb.toDto(subscription.getSubscriptionUser()))
//                .build();
//    }
//
//
//    @Override
//    public Subscription mapFrom(SubscriptionDto subscription) {
//        return SubscriptionDto.builder()
//                .id(subscription.getId())
//                .subscriptionDate(subscription.getSubscriptionDate())
//                .subscriberUser(USER_MAPPER_FOR_DATA_BASE.mapFrom(subscription.getSubscriberUser()))
//                .subscriptionUser(userMapperForWeb.toDto(subscription.getSubscriptionUser()))
//                .build();
//    }
//}
