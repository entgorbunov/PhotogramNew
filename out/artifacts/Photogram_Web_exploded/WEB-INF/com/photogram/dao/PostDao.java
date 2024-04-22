package com.photogram.dao;

import com.photogram.daoException.DaoException;
import com.photogram.entity.Post;
import com.photogram.entity.User;
import com.photogram.mapper.UserMapper;
import com.photogram.service.UserService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDao implements PostDaoInterface<Post, Long> {
    private static volatile PostDao instance;
    private final UserDao userDao = UserDao.getInstance();

                private static final String USER_NOT_FOUND_WITH_ID = "User not found with ID: ";

    private static final String FIND_ALL_SQL = """
            SELECT photogram.public.posts.id,
                       posts.user_id,
                       posts.caption,
                       posts.post_time,
                       posts.image_url
                       from posts
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                       where id = ?
            """;


    private static final String INSERT_NEW_POST = """
            INSERT INTO photogram.public.posts (id, user_id, caption, post_time, image_url) VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_POST = """
            UPDATE photogram.public.posts SET id = ?, user_id = ?, caption = ?, post_time = ?, image_url = ? WHERE id = ?
            """;

    private static final String DELETE_POST = """
            DELETE from photogram.public.posts where id = ?
            """;


    public static PostDao getInstance() {
        PostDao localInstance = instance;
        if (localInstance == null) {
            synchronized (PostDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PostDao();
                }
            }
        }
        return localInstance;
    }

    @Override
    public boolean delete(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(DELETE_POST)) {
            preparedStatement.setLong(1, id);
            var changedData = preparedStatement.executeUpdate();
            if (changedData == 0) throw new DaoException("Post has not been deleted");
            return changedData > 0;
        } catch (SQLException e) {
            throw new DaoException("Deleting post failed", e);
        }
    }

    @Override
    public void save(Post post, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_POST, Statement.RETURN_GENERATED_KEYS)) {
            var affectedRows = setInfoToPost(post, preparedStatement);
            if (affectedRows == 0) {
                throw new DaoException("Creating post failed");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong("id"));
                } else {
                    throw new DaoException("Creating post failed, no ID get");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Saving post failed", e);
        }
    }

    @Override
    public void update(Post post, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POST)) {
            var affectedRows = setInfoToPost(post, preparedStatement);
            if (affectedRows == 0) {
                throw new DaoException("Updating post failed");
            }
        } catch (SQLException e) {
            throw new DaoException("Updating post failed", e);
        }

    }


    @Override
    public Optional<Post> findById(Long userId, Connection connection) {
        Post post = new Post();
        ResultSet resultSet;
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            resultSet = preparedStatement.executeQuery();
            var optionalUser = userDao.findById(userId, connection);

            if (resultSet.next()) {
                return Optional.of(createPost(resultSet, optionalUser.orElseThrow()));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding post by User ID failed", e);
        }
        return Optional.ofNullable(post);
    }


    @Override
    public List<Post> findAll(Long userId, Connection connection) {
        List<Post> postList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)) {
            var userList = userDao.findAll(userId, connection);

            while (resultSet.next()) {
                postList.add(createPost(resultSet, userList.get(Math.toIntExact(userId))));
            }
        } catch (SQLException e) {
            throw new DaoException("Finding all posts failed", e);
        }
        return postList;
    }

    @Override
    public List<Post> findAllByUserId(Long userId, Connection connection) {
        List<Post> postList = new ArrayList<>();
        try {
            try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
                preparedStatement.setObject(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                var optionalUser = userDao.findById(userId, connection);

                while (resultSet.next()) {
                    postList.add(createPost(resultSet, optionalUser.orElseThrow()));
                }
                return postList;
            }
        } catch (SQLException e) {
            throw new DaoException("Finding all posts by user_id is not working, error.", e);
        }
    }

    private Post createPost(ResultSet resultSet, User user) {
        try {
            return new Post(resultSet.getLong("id"),
                    user,
                    resultSet.getString("caption"),
                    resultSet.getObject("post_time", Timestamp.class).toLocalDateTime(),
                    resultSet.getString("image_url"));
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
            preparedStatement.setLong(6, post.getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Setting info into post failed", e);
        }
    }
}
