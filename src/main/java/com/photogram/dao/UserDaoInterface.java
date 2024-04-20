package com.photogram.dao;

import com.photogram.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDaoInterface<U, L> extends BaseDaoInterface<U, L> {
    List<User> findAll();
    Optional<User> findById(L id);
}
