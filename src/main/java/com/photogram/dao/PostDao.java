package com.photogram.dao;

import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Post;
import com.photogram.exceptions.DaoException;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PostDao implements PostDaoInterface<Post, Long> {


    private static final String FIND_ALL_SQL = """
            SELECT id,
                   user_id,
                   caption,
                   post_time,
                   image_url,
                   is_deleted,
                   text
                   from posts
            """;


    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                       where id = ?
            """;


    private static final String INSERT_NEW_POST = """
            INSERT INTO photogram.public.posts (user_id, caption, post_time, image_url, text) VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_POST = """
            UPDATE photogram.public.posts SET user_id = ?, caption = ?, post_time = ?, image_url = ?, text = ? WHERE id = ?
            """;

    private static final String SOFT_DELETE_POST = """
            UPDATE photogram.public.posts SET is_deleted = TRUE where id = ?
            """;
    public static final String WHERE_IS_DELETED_FALSE = " WHERE is_deleted = FALSE";
    public static final String WHERE_IS_DELETED_FALSE_AND_USER_ID = " WHERE is_deleted = FALSE AND user_id = ?";

    public static final AtomicReference<PostDao> POST_DAO_ATOMIC_REFERENCE = new AtomicReference<>();

    public static PostDao getPostDaoAtomicReference() {
        POST_DAO_ATOMIC_REFERENCE.get();
        if (POST_DAO_ATOMIC_REFERENCE.get() == null) {
            PostDao newPostDao = new PostDao();
            if (POST_DAO_ATOMIC_REFERENCE.compareAndSet(null, newPostDao)) {
                return newPostDao;
            } else {
                return POST_DAO_ATOMIC_REFERENCE.get();
            }
        }
        return POST_DAO_ATOMIC_REFERENCE.get();
    }

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
                if (generatedKeys.next()) post.setId(generatedKeys.getLong(1));
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
    public Optional<Post> findById(Long postId) {
        Post post = new Post();
        ResultSet resultSet;
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, postId);
            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                return Optional.of(buildPost(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding post by User ID failed", e);
        }

        return Optional.empty();

    }

    public List<Post> findAllById(Long userId) {
        List<Post> posts = new ArrayList<>();
        ResultSet resultSet;
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                posts.add(buildPost(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding posts by User ID failed", e);
        }

        return posts;

    }


    @Override
    public List<Post> findAll() {
        List<Post> postList = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.get();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)) {

            while (resultSet.next()) {
                postList.add(buildPost(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding all posts failed", e);
        }
        return postList;
    }

    public List<Post> findAllActiveByUserId(Long userId) {
        List<Post> postList = new ArrayList<>();
        String findAllActiveByUserIdSQL = FIND_ALL_SQL + WHERE_IS_DELETED_FALSE_AND_USER_ID;
        try (
                Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(findAllActiveByUserIdSQL)) {

            preparedStatement.setLong(1, userId);  // Set the userId parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    postList.add(buildPost(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Finding all active posts by user ID failed", e);
        }
        return postList;
    }


    public List<Post> findAllActive() {
        List<Post> postList = new ArrayList<>();
        String findAllActiveSQL = FIND_ALL_SQL + WHERE_IS_DELETED_FALSE;
        try (
                Connection connection = ConnectionManager.get();
                PreparedStatement statement = connection.prepareStatement(findAllActiveSQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                postList.add(buildPost(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding all active posts failed", e);
        }
        return postList;
    }


    private Post buildPost(ResultSet resultSet) throws SQLException {
        try {
            return new Post(resultSet.getLong("id"),
                    UserDao.getUserDaoAtomicReference().findById(resultSet.getLong("user_id")).orElseThrow(() -> new DaoException("Couldn't find the user")),
                    resultSet.getString("caption"),
                    resultSet.getObject("post_time", LocalDateTime.class),
                    resultSet.getString("image_url"),
                    resultSet.getBoolean("is_deleted"),
                    resultSet.getString("text"));
        } catch (SQLException e) {
            throw new DaoException("Creating post failed", e);
        }
    }

    private static int setInfoToPost(Post post, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setLong(1, post.getUser().getId());
            preparedStatement.setString(2, post.getCaption());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(post.getPostTime()));
            preparedStatement.setString(4, post.getImageUrl());
            preparedStatement.setString(5, post.getText());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Setting info into post failed", e);
        }
    }


}
