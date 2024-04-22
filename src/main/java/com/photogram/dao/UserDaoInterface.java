package com.photogram.dao;

import java.util.List;
import java.util.Optional;

public interface UserDaoInterface<U, K> extends BaseDaoInterface<U, K> {
    List<U> findAll();
    Optional<U> findById(K id);

}
