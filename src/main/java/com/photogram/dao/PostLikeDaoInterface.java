package com.photogram.dao;

import com.photogram.entity.PostLike;

import java.util.Optional;

public interface PostLikeDaoInterface<P, L> extends BaseDaoInterface<P, L> {
    Optional<PostLike> findById(L id);
}
