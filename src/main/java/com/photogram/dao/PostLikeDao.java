package com.photogram.dao;

import com.photogram.daoException.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Post;
import com.photogram.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLikeDao {
    @Getter
    private static final PostLikeDao INSTANCE = PostLikeDao.getINSTANCE();
    private final UserDao userDao = UserDao.getInstance();
    private final PostDao postDao = PostDao.getInstance();

    private static final String INSERT_LIKE = "INSERT INTO postlikes (user_id, post_id, like_time) VALUES (?, ?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM postlikes WHERE user_id = ? AND post_id = ?";
    private static final String CHECK_LIKE = "SELECT count(*) FROM postlikes WHERE user_id = ? AND post_id = ?";
    private static final String COUNT_LIKES = "SELECT count(*) FROM postlikes WHERE post_id = ?";

    public void addLike(Long userId, Long postId) {
        User user = userDao.findById(userId).orElseThrow(() -> new DaoException("User not found"));
        Post post = postDao.findById(postId).orElseThrow(() -> new DaoException("Post not found"));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LIKE)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, post.getId());
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Failed to insert like");
            }
        } catch (SQLException e) {
            throw new DaoException("Error adding like to post", e);
        }
    }


    public void removeLike(Long userId, Long postId) {
        User user = userDao.findById(userId).orElseThrow(() -> new DaoException("User not found"));
        Post post = postDao.findById(postId).orElseThrow(() -> new DaoException("Post not found"));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, post.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error removing like from post", e);
        }
    }

    public boolean isLikedByUser(Long userId, Long postId) {
        User user = userDao.findById(userId).orElseThrow(() -> new DaoException("User not found"));
        Post post = postDao.findById(postId).orElseThrow(() -> new DaoException("Post not found"));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LIKE)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, post.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error checking if the user liked post", e);
        }
        return false;
    }

    public int countLikes(Long postId) {
        Post post = postDao.findById(postId).orElseThrow(() -> new DaoException("Post not found"));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_LIKES)) {
            preparedStatement.setLong(1, post.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error counting likes for the post", e);
        }
        return 0;
    }



}
