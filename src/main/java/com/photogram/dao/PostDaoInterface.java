package com.photogram.dao;

import com.photogram.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostDaoInterface<P, L> extends BaseDaoInterface<P, L>{
    List<Post> findAll(Long userId);
    Optional<Post> findById(L id);
}
