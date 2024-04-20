package com.photogram.dao;

import com.photogram.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageDaoInterface<I, L> extends BaseDaoInterface<I, L>{
    List<Image> findAll(Long userId);
    Optional<Image> findById(L id);
}
