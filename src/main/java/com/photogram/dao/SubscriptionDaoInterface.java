package com.photogram.dao;

import com.photogram.entity.User;

import java.util.List;

public interface SubscriptionDaoInterface<S, K> extends BaseDaoInterface<S, K> {

    List<User> findUsersFollowingById(K userId);
    List<User> findUsersFollowersById(K userId);


}
