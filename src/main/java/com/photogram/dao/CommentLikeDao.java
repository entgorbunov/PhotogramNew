package com.photogram.dao;

import com.photogram.entity.CommentLike;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentLikeDao implements CommentLikeDaoInterface<CommentLike, Long> {
    private static volatile CommentLikeDao instance;

    public static CommentLikeDao getInstance() {
        if (instance == null) {
            synchronized (CommentLikeDao.class) {
                if (instance == null) {
                    instance = new CommentLikeDao();
                }
            }
        }
        return instance;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void save(CommentLike entity) {

    }

    @Override
    public void update(CommentLike entity) {

    }

    @Override
    public Optional<CommentLike> findById(Long id) {
        return Optional.empty();
    }


}