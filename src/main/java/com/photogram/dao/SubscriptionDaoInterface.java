package com.photogram.dao;

import com.photogram.entity.User;

import java.util.List;

public interface SubscriptionDaoInterface<S, L> extends BaseDaoInterface<S, L> {
    List<User> findUsersFollowingById(Long userId);
    List<User> findUsersFollowersById(Long userId);

}
