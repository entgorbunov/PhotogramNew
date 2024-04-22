package com.photogram.dao.likeEntityDao;

import com.photogram.dao.CommentDao;
import com.photogram.dao.UserDao;
import com.photogram.daoException.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.CommentForPost;
import com.photogram.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentLikeDao implements CommentLikeDaoInterface {
    @Getter
    private static final CommentLikeDao INSTANCE = new CommentLikeDao();
    private static final String COMMENT_NOT_FOUND = "Comment not found";

    private static final String INSERT_LIKE = "INSERT INTO commentlikes (user_id, comment_id, like_time) VALUES (?, ?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM commentlikes WHERE user_id = ? AND comment_id = ?";
    private static final String CHECK_LIKE = "SELECT count(*) FROM commentlikes WHERE user_id = ? AND comment_id = ?";
    private static final String COUNT_LIKES = "SELECT count(*) FROM commentlikes WHERE comment_id = ?";



    @Override
    public void delete(Long commentId, Long userId) {
        User user = UserDao.getInstance().findById(userId).orElseThrow(() -> new DaoException("User not found"));
        CommentForPost comment = CommentDao.getINSTANCE().findById(commentId).orElseThrow(() -> new DaoException(COMMENT_NOT_FOUND));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, comment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error removing like from comment", e);
        }
    }

    @Override
    public void save(Long userId, Long commentId) {
        User user = UserDao.getInstance().findById(userId).orElseThrow(() -> new DaoException("User not found"));
        CommentForPost comment = CommentDao.getINSTANCE().findById(commentId).orElseThrow(() -> new DaoException(COMMENT_NOT_FOUND));
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LIKE)) {
            createLike(user, preparedStatement, comment.getId());
        } catch (SQLException e) {
            throw new DaoException("Error adding like to comment", e);
        }
    }

    static void createLike(User user, PreparedStatement preparedStatement, Long id) throws SQLException {
        preparedStatement.setLong(1, user.getId());
        preparedStatement.setLong(2, id);
        preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
            throw new DaoException("Failed to insert like");
        }
    }

    @Override
    public Boolean isLikedByUser(Long userId, Long commentId) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LIKE)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, commentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error checking if the comment is liked by user", e);
        }
        return false;
    }

    @Override
    public Long countLikes(Long commentId) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_LIKES)) {
            preparedStatement.setLong(1, commentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error counting likes for the comment", e);
        }
        return 0L;
    }



}