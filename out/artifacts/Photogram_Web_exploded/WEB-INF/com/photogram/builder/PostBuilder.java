package com.photogram.builder;

import com.photogram.dao.UserDao;
import com.photogram.daoException.DaoException;
import com.photogram.entity.Post;
import com.photogram.dataSource.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PostBuilder {

    public static Post postBuilder(Long id, Long userId, String caption, LocalDateTime postTime, String imageUrl) {
        final UserDao userDao = UserDao.getInstance();

        try(Connection connection = ConnectionManager.open();) {
            var userOptional = userDao.findById(userId, connection);
            if (userOptional.isPresent()) {
                var user = userOptional.get();
                return new Post(id, user, caption, postTime, imageUrl);
            } else throw new DaoException("User with ID " + userId + " not found.");

        } catch (SQLException e) {
            throw new DaoException("Error building post", e);
        }

    }
}
