package com.photogram.dao.likeDao;

import com.photogram.exceptions.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Like;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeDao {
    @Getter
    private static final LikeDao INSTANCE = new LikeDao();

    private static final String DELETE_LIKE = "DELETE FROM likes WHERE user_id = ? AND (post_id = ? OR comment_id = ?)";
    private static final String CHECK_LIKE = "SELECT count(*) FROM likes WHERE user_id = ? AND (post_id = ? OR comment_id = ?)";
    private static final String COUNT_LIKES_FOR_POST = "SELECT count(*) FROM likes WHERE post_id = ?";
    private static final String COUNT_LIKES_FOR_COMMENT = "SELECT count(*) FROM likes WHERE comment_id = ?";
    private static final String INSERT_LIKE = "INSERT INTO likes (user_id, post_id, comment_id, like_time) VALUES (?, ?, ?, ?)";

    public void save(Like like) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LIKE)) {
            preparedStatement.setLong(1, like.getUserId());
            preparedStatement.setObject(2, like.getPostId(), Types.BIGINT);
            preparedStatement.setObject(3, like.getCommentId(), Types.BIGINT);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(like.getLikeTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error adding like", e);
        }
    }

    public void delete(Like like) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {
            preparedStatement.setLong(1, like.getUserId());
            preparedStatement.setObject(2, like.getPostId(), Types.BIGINT);
            preparedStatement.setObject(3, like.getCommentId(), Types.BIGINT);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error removing like", e);
        }
    }

    public boolean isLikedByUser(Like like) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LIKE)) {
            preparedStatement.setLong(1, like.getUserId());
            preparedStatement.setObject(2, like.getPostId(), Types.BIGINT);
            preparedStatement.setObject(3, like.getCommentId(), Types.BIGINT);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Error checking like", e);
        }
    }

    public Long countLikes(Long postId, Long commentId) {
        String sql = postId != null ? COUNT_LIKES_FOR_POST : COUNT_LIKES_FOR_COMMENT;
        Long entityId = postId != null ? postId : commentId;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, entityId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong(1) : 0L;
            }
        } catch (SQLException e) {
            throw new DaoException("Error counting likes", e);
        }
    }
}
