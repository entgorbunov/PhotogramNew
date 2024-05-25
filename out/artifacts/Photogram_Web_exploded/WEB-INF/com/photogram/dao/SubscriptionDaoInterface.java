package com.photogram.dao;

import com.photogram.entity.Subscription;

import java.util.List;

public interface SubscriptionDaoInterface<S, K> extends BaseDaoInterface<S, K> {

    List<Subscription> findSubscriptions(K userId);
    List<Subscription> findSubscribers(K userId);


}
