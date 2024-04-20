package com.photogram.dao;


import com.photogram.entity.CommentForPost;

import java.util.Optional;

public interface CommentDaoInterface<C, K> extends BaseDaoInterface<C, K>{

    Optional<CommentForPost> findById(K id);
}
