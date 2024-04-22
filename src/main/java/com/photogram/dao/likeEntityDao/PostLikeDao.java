package com.photogram.dao.likeEntityDao;

import com.photogram.dao.PostDao;
import com.photogram.dao.UserDao;
import com.photogram.daoException.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Post;
import com.photogram.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostLikeDao implements PostLikeDaoInterface {
    @Getter
    private static final PostLikeDao INSTANCE = new PostLikeDao();
    public static final String USER_NOT_FOUND = "User not found";
    public static final String POST_NOT_FOUND = "Post not found";

    private static final String INSERT_LIKE = "INSERT INTO postlikes (user_id, post_id, like_time) VALUES (?, ?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM postlikes WHERE user_id = ? AND post_id = ?";
    private static final String CHECK_LIKE = "SELECT count(*) FROM postlikes WHERE user_id = ? AND post_id = ?";
    private static final String COUNT_LIKES = "SELECT count(*) FROM postlikes WHERE post_id = ?";

    @Override
    public Long countLikes(Long postId) {
        Post post = PostDao.getInstance().findById(postId).orElseThrow(() -> new DaoException(POST_NOT_FOUND));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_LIKES)) {
            preparedStatement.setLong(1, post.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error counting likes for the post", e);
        }
        return 0L;
    }

    @Override
    public void delete(Long userId, Long postId) {
        User user = UserDao.getInstance().findById(userId).orElseThrow(() -> new DaoException(USER_NOT_FOUND));
        Post post = PostDao.getInstance().findById(postId).orElseThrow(() -> new DaoException(POST_NOT_FOUND));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, post.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error removing like from post", e);
        }
    }

    @Override
    public void save(Long userId, Long postId) {
        User user = UserDao.getInstance().findById(userId).orElseThrow(() -> new DaoException(USER_NOT_FOUND));
        Post post = PostDao.getInstance().findById(postId).orElseThrow(() -> new DaoException(POST_NOT_FOUND));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LIKE)) {
            CommentLikeDao.createLike(user, preparedStatement, post.getId());
        } catch (SQLException e) {
            throw new DaoException("Error adding like to post", e);
        }
    }

    @Override
    public Boolean isLikedByUser(Long userId, Long postId) {
        User user = UserDao.getInstance().findById(userId).orElseThrow(() -> new DaoException(USER_NOT_FOUND));
        Post post = PostDao.getInstance().findById(postId).orElseThrow(() -> new DaoException(POST_NOT_FOUND));
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



}
