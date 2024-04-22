package com.photogram.dao;

import java.util.List;
import java.util.Optional;

public interface PostDaoInterface<P, K> extends BaseDaoInterface<P, K>{
    List<P> findAll();
    Optional<P> findById(K id);
}
