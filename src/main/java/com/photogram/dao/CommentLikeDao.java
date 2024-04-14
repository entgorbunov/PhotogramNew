package com.photogram.dao;

import com.photogram.entity.CommentLike;
import com.photogram.daoInterfaces.CommentLikeDaoInterface;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.util.List;
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
    public boolean delete(Long id, Connection connection) {
        return false;
    }

    @Override
    public void save(CommentLike entity, Connection connection) {

    }

    @Override
    public void update(CommentLike entity, Connection connection) {

    }

    @Override
    public Optional<CommentLike> findById(Long id, Connection connection) {
        return Optional.empty();
    }

    @Override
    public List<CommentLike> findAll(Connection connection) {
        return null;
    }
    // DAO methods
}