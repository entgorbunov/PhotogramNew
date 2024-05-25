package com.photogram.service;

import com.photogram.dao.SubscriptionDao;
import com.photogram.dao.UserDao;
import com.photogram.dto.SubscriptionDto;
import com.photogram.entity.Subscription;
import com.photogram.mapper.SubscriptionMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SubscriptionService implements BaseServiceInterface<SubscriptionDto, Long> {

    private static final SubscriptionDao SUBSCRIPTION_DAO = SubscriptionDao.getSubscriptionDaoAtomicReference();
    private static final AtomicReference<SubscriptionService> SUBSCRIPTION_SERVICE_ATOMIC_REFERENCE = new AtomicReference<>();
    private static final SubscriptionMapper SUBSCRIPTION_MAPPER = SubscriptionMapper.getSubscriptionMapper();
    private static final UserDao USER_DAO = UserDao.getUserDaoAtomicReference();

    public static SubscriptionService getInstance() {
        if (SUBSCRIPTION_SERVICE_ATOMIC_REFERENCE.get() == null) {
            SubscriptionService instance = new SubscriptionService();

            if (SUBSCRIPTION_SERVICE_ATOMIC_REFERENCE.compareAndSet(null, instance)) {
                return instance;
            } else {
                return SUBSCRIPTION_SERVICE_ATOMIC_REFERENCE.get();
            }
        }
        return SUBSCRIPTION_SERVICE_ATOMIC_REFERENCE.get();
    }


    @Override
    public void delete(Long userId) {

    }

    public void delete(Long userId, Long followerId) {
        SUBSCRIPTION_DAO.delete(userId, followerId);
    }

    @Override
    public Long create(SubscriptionDto subscriptionDto) {
        Subscription entity = SUBSCRIPTION_MAPPER.toEntity(subscriptionDto);
        SUBSCRIPTION_DAO.save(entity);
        return entity.getUserId();
    }

    @Override
    public SubscriptionDto updateWithImage(SubscriptionDto subscriptionDto) {
        return null;
    }

    @Override
    public SubscriptionDto findById(Long userId) {
        return null;
    }


    public List<SubscriptionDto> getSubscriptionsDto(Long userId) {
        List<SubscriptionDto> subscriptionDtoList = new ArrayList<>();
        List<Subscription> subscriptions = SUBSCRIPTION_DAO.findSubscriptions(userId);
        if (!subscriptions.isEmpty()) {
            SubscriptionDto dto = SUBSCRIPTION_MAPPER.toDto(subscriptions.get(0));
            subscriptionDtoList.add(dto);
        }
        return subscriptionDtoList;
    }


    @Override
    public List<SubscriptionDto> findAll() {
        return List.of();
    }
}
