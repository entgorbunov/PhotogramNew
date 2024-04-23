package com.photogram.dao;


import com.photogram.entity.CommentForPost;

import java.util.List;
import java.util.Optional;

public interface CommentDaoInterface<K, C> extends BaseDaoInterface<K, C> {


    List<CommentForPost> findAll();
    Optional<CommentForPost> findById(Long id);
}
