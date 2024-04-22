package com.photogram.builder;

import com.photogram.dao.PostDao;
import com.photogram.dao.UserDao;
import com.photogram.daoException.DaoException;
import com.photogram.entity.Image;
import com.photogram.dataSource.ConnectionManager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ImageBuilder {
    public static Image imageBuilder(Long id, String path, Long postId, Long userId, LocalDateTime uploadedTime) {
        final UserDao userDao = UserDao.getInstance();
        final PostDao postDao = PostDao.getInstance();
        try (var connection = ConnectionManager.open()) {
            var userOptional = userDao.findById(userId, connection);
            var postOptional = postDao.findById(postId, connection);
            if (userOptional.isPresent() && postOptional.isPresent()) {
                var user = userOptional.get();
                var post = postOptional.get();
                return new Image(id, path, post, user, uploadedTime);
            } else throw new DaoException("User with ID " + userId + " and post with ID " + postId + " not found.");

        } catch (SQLException e) {
            throw new DaoException("Error building Image Object",e);
        }
    }
}
