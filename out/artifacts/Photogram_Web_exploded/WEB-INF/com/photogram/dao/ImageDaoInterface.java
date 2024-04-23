package com.photogram.dao;

import java.util.List;
import java.util.Optional;

public interface ImageDaoInterface<I, K> extends BaseDaoInterface<I, K>{
    List<I> findAll();
    Optional<I> findById(K id);
}
