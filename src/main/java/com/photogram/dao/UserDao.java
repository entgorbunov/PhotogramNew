package com.photogram.dao;

import com.photogram.exceptions.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Gender;
import com.photogram.entity.Role;
import com.photogram.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements UserDaoInterface<User, Long> {

    private static final String SELECT_ALL_USERS = "SELECT id, username, profile_picture, bio, is_private, image_url, is_active, email, password, role, gender, birthday FROM photogram.public.Users";

    private static final String SELECT_USER_BY_ID = SELECT_ALL_USERS + " WHERE id = ? ";

    private static final String INSERT_NEW_USER = "INSERT INTO photogram.public.Users (username, profile_picture, bio, is_private, image_url, is_active, email, password, role, gender, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE Users SET username = ?, profile_picture = ?, bio = ?, is_private = ?, image_url = ?, is_active = ?, email = ?, password = ?, role = ?, gender = ?, birthday = ? WHERE id = ?";
    private static final String SOFT_DELETE_USER = "UPDATE Users SET is_active = FALSE WHERE id = ?";
    public static final String GET_BY_EMAIL_AND_PASSWORD = "SELECT id, username, profile_picture, bio, is_private, image_url, is_active, email, password, role, gender, birthday FROM photogram.public.Users WHERE email = ? AND password = ?";

    @Getter
    private static final UserDao instance = new UserDao();

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS)) {
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding all users: " + e.getMessage(), e);
        }
        return users;
    }


    @Override
    public Optional<User> findById(Long id) {
        try (
                Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding user by ID " + id + ": " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void save(User user) {
        try (
                Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS)) {
            setUser(user, preparedStatement);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Creating user failed.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error saving user: " + e.getMessage(), e);
        }
    }

    @Override
    public User update(User user) {
        Connection connection = ConnectionManager.get();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
            setUser(user, preparedStatement);
            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new DaoException("Error updating user with ID " + user.getId() + ": " + e.getMessage(), e);

        }
    }

    @Override
    public void delete(Long id) {
        try (
                Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(SOFT_DELETE_USER)) {
            preparedStatement.setLong(1, id);
            int changedData = preparedStatement.executeUpdate();
            if (changedData == 0) throw new DaoException("No user to delete with ID " + id);
        } catch (SQLException e) {
            throw new DaoException("Error deleting user with ID " + id + ": " + e.getMessage(), e);
        }
    }

    protected User buildUser(ResultSet resultSet) {
        try {
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("profile_picture"),
                    resultSet.getString("bio"),
                    resultSet.getBoolean("is_private"),
                    resultSet.getString("image_url"),
                    resultSet.getBoolean("is_active"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role").toUpperCase()),
                    Gender.valueOf(resultSet.getString("gender").toUpperCase()),
                    resultSet.getObject("birthday", LocalDateTime.class));
        } catch (SQLException e) {
            throw new DaoException("Error creating user: " + e.getMessage(), e);
        }
    }

    private void setUser(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getProfilePicture());
        preparedStatement.setString(3, user.getBio());
        preparedStatement.setBoolean(4, user.getIsPrivate());
        preparedStatement.setString(5, user.getImage());
        preparedStatement.setBoolean(6, user.getIsActive());
        preparedStatement.setString(7, user.getEmail());
        preparedStatement.setString(8, user.getPassword());
        preparedStatement.setString(9, user.getRole().name());
        preparedStatement.setString(10, user.getGender().name());
        preparedStatement.setTimestamp(11, Timestamp.valueOf(user.getBirthday()));
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_BY_EMAIL_AND_PASSWORD)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException("Error finding user by email and password: " + e.getMessage(), e);
        }
    }
}