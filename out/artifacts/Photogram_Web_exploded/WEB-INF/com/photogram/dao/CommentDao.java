package com.photogram.dao;

import com.photogram.Exceptions.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.CommentForPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDao implements CommentDaoInterface<CommentForPost, Long> {
    @Getter
    private static final CommentDao INSTANCE = new CommentDao();

    private static final String FIND_ALL_SQL = """
            SELECT photogram.public.comments.id,
                       photogram.public.comments.post_id,
                       photogram.public.comments.user_id,
                       photogram.public.comments.text,
                       photogram.public.comments.comment_time
                       from photogram.public.comments
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                       where id = ?
            """;

    private static final String INSERT_NEW_COMMENT = """
            INSERT INTO photogram.public.comments (id, post_id, user_id, text, comment_time) VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_COMMENT = """
            UPDATE photogram.public.comments SET id = ?, post_id = ?, user_id = ?, text = ?, comment_time = ? WHERE id = ?
            """;

    private static final String DELETE_COMMENT = """
            DELETE from photogram.public.comments where id = ?
            """;


    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_COMMENT)) {
            preparedStatement.setLong(1, id);
            var changedData = preparedStatement.executeUpdate();
            if (changedData == 0) throw new DaoException("Comment has not been deleted");
        } catch (SQLException e) {
            throw new DaoException("Failed to delete comment", e);
        }
    }

    @Override
    public void save(CommentForPost comment) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(INSERT_NEW_COMMENT, Statement.RETURN_GENERATED_KEYS)) {
            var affectedRows = setInfoToComment(comment, preparedStatement);
            if (affectedRows == 0) {
                throw new DaoException("Creating comment failed");
            }
            try (var generatedKeys = preparedStatement.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getLong("id"));
                } else {
                    throw new DaoException("Creating comment failed, no ID obtained");
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Failed to save comment", e);
        }
    }


    @Override
    public CommentForPost update(CommentForPost comment) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMMENT)) {
            var affectedRows = setInfoToComment(comment, preparedStatement);
            if (affectedRows == 0) {
                throw new DaoException("Updating post failed");
            }
            return comment;
        } catch (SQLException e) {
            throw new DaoException("Failed to update comment", e);
        }
    }

    @Override
    public Optional<CommentForPost> findById(Long id) {
        CommentForPost commentForPost = new CommentForPost();
        ResultSet resultSet;
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createComment(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find comment", e);
        }
        return Optional.ofNullable(commentForPost);
    }

    @Override
    public List<CommentForPost> findAll() {
        List<CommentForPost> commentForPostList = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             var statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)) {
            while (resultSet.next()) {
                commentForPostList.add(createComment(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find comment", e);
        }
        return commentForPostList;
    }


    private CommentForPost createComment(ResultSet resultSet) {
        try {
            return new CommentForPost(resultSet.getLong("id"),
                    PostDao.getInstance().findById(resultSet.getLong("post_id")).orElseThrow(() ->
                            new DaoException("Post not found")),
                    UserDao.getInstance().findById(resultSet.getLong("user_id")).orElseThrow(() ->
                            new DaoException("User not found")),
                    resultSet.getString("text"),
                    resultSet.getTimestamp("comment_time").toLocalDateTime());
        } catch (SQLException e) {
            throw new DaoException("Failed to create comment", e);
        }
    }


    private int setInfoToComment(CommentForPost comment, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setLong(1, comment.getId());
            preparedStatement.setLong(2, comment.getPost().getId());
            preparedStatement.setLong(3, comment.getUser().getId());
            preparedStatement.setString(4, comment.getText());
            preparedStatement.setObject(5, comment.getCommentTime());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to set info to comment", e);
        }
    }
}

