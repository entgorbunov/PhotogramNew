package com.photogram.dao;

import com.photogram.entity.CommentLike;

import java.util.Optional;

public interface CommentLikeDaoInterface<C, K> extends BaseDaoInterface<C, K> {
    Optional<CommentLike> findById(K id);
}
