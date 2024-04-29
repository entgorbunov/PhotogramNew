package com.photogram.dao;

import com.photogram.Exceptions.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Post;
import com.photogram.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PostDao implements PostDaoInterface<Post, Long> {
    @Getter
    private static final PostDao instance = new PostDao();


    private static final String FIND_ALL_SQL = """
            SELECT id,
                   user_id,
                   caption,
                   post_time,
                   image_url,
                   is_deleted
                   from posts
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                       where id = ?
            """;


    private static final String INSERT_NEW_POST = """
            INSERT INTO photogram.public.posts (id, user_id, caption, post_time, image_url, is_deleted) VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_POST = """
            UPDATE photogram.public.posts SET id = ?, user_id = ?, caption = ?, post_time = ?, image_url = ?, is_deleted = ? WHERE id = ?
            """;

    private static final String SOFT_DELETE_POST = """
            UPDATE photogram.public.posts SET is_deleted = TRUE where id = ?
            """;


    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SOFT_DELETE_POST)) {
            preparedStatement.setLong(1, id);
            var changedData = preparedStatement.executeUpdate();
            if (changedData == 0) throw new DaoException("Post has not been deleted");
        } catch (SQLException e) {
            throw new DaoException("Deleting post failed", e);
        }
    }

    @Override
    public void save(Post post) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_POST, Statement.RETURN_GENERATED_KEYS)) {
            var affectedRows = setInfoToPost(post, preparedStatement);
            if (affectedRows == 0) {
                throw new DaoException("Creating post failed");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) post.setId(generatedKeys.getLong("id"));
                else throw new DaoException("Creating post failed, no ID get");
            }
        } catch (SQLException e) {
            throw new DaoException("Saving post failed", e);
        }
    }

    @Override
    public Post update(Post post) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POST)) {
            var affectedRows = setInfoToPost(post, preparedStatement);
            if (affectedRows == 0) throw new DaoException("Updating post failed");
            return post;
        } catch (SQLException e) {
            throw new DaoException("Updating post failed", e);
        }

    }


    @Override
    public Optional<Post> findById(Long userId) {
        Post post = new Post();
        ResultSet resultSet;
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                User user = UserDao.getInstance().findById(userId)
                        .orElseThrow(() ->
                                new DaoException("Couldn't find the user"));

                return Optional.of(createPost(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding post by User ID failed", e);
        }

        return Optional.ofNullable(post);

    }


    @Override
    public List<Post> findAll() {
        List<Post> postList = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.get();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)) {

            while (resultSet.next()) {
                postList.add(createPost(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding all posts failed", e);
        }
        return postList;
    }


    private Post createPost(ResultSet resultSet) throws SQLException {
        try {
            return new Post(resultSet.getLong("id"),
                    UserDao.getInstance().findById(resultSet.getLong("user_id")).orElseThrow(() -> new DaoException("Couldn't find the user")),
                    resultSet.getString("caption"),
                    resultSet.getObject("post_time", LocalDateTime.class),
                    resultSet.getString("image_url"), resultSet.getBoolean("is_deleted"));
        } catch (SQLException e) {
            throw new DaoException("Creating post failed", e);
        }
    }

    private static int setInfoToPost(Post post, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setLong(1, post.getId());
            preparedStatement.setLong(2, post.getUser().getId());
            preparedStatement.setString(3, post.getCaption());
            preparedStatement.setObject(4, post.getPostTime());
            preparedStatement.setString(5, post.getImageUrl());
            preparedStatement.setBoolean(6, post.getIsDeleted());
            preparedStatement.setLong(7, post.getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Setting info into post failed", e);
        }
    }


}
