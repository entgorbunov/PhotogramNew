package com.photogram.dao;

import com.photogram.dataSource.ConnectionManager;
import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Gender;
import com.photogram.entity.Role;
import com.photogram.entity.User;
import com.photogram.exceptions.DaoException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements UserDaoInterface<User, Long> {

    private static final String SELECT_ALL_USERS = "SELECT id, username, profile_picture, bio, image_url, is_active, email, password, role_id, gender_id, birthday, name FROM photogram.public.Users";

    private static final String SELECT_USER_BY_ID = SELECT_ALL_USERS + " WHERE id = ? ";

    private static final String INSERT_NEW_USER = "INSERT INTO photogram.public.Users (username, profile_picture, bio,  image_url, email, password, gender_id, birthday, name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE Users SET username = ?, profile_picture = ?, bio = ?, image_url = ?, email = ?, password = ?, gender_id = ?, birthday = ?, name = ?, role_id = ? WHERE id = ?;";

    private static final String SOFT_DELETE_USER = "UPDATE Users SET is_active = FALSE WHERE id = ?";
    private static final String GET_BY_EMAIL_AND_PASSWORD = "SELECT id, username, profile_picture, bio, image_url, is_active, email, password, role_id, gender_id, birthday, name FROM photogram.public.Users WHERE email = ? AND password = ?";
    private static final String RESTORE_USER = "UPDATE Users SET is_active = TRUE WHERE id = ?";
    private static final String WHERE_ID = " WHERE id = ?";

    private static final String FIND_NOT_FOLLOWERS = """
            SELECT u.id \
            FROM users u \
            LEFT JOIN followers f ON u.id = f.user_id AND f.follower_id = ? \
            WHERE f.user_id IS NULL AND u.id != ?""";

    private static final String FIND_SUBSCRIPTIONS = """
        SELECT user_id
        FROM followers
        WHERE follower_id = ?""";

    public static final String USERS_ENT_DESKTOP_PICTURES = "/Users/ent/Desktop/Pictures/users/";
    private static final AtomicReference<UserDao> USER_DAO_ATOMIC_REFERENCE = new AtomicReference<>();

    public static UserDao getUserDaoAtomicReference() {
        UserDao current = USER_DAO_ATOMIC_REFERENCE.get();
        if (current == null) {
            UserDao newUserDao = new UserDao();
            if (USER_DAO_ATOMIC_REFERENCE.compareAndSet(null, newUserDao)) {
                return newUserDao;
            } else {
                return USER_DAO_ATOMIC_REFERENCE.get();
            }
        }
        return current;
    }

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
        try (Connection connection = ConnectionManager.get();
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

    public User update(UserDtoFromWeb userDtoFromWeb) {
        if (userDtoFromWeb.getId() == null) {
            throw new DaoException("User ID is mandatory for updating a user.");
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE Users SET ");
        List<Object> parameters = new ArrayList<>();
        List<String> updateClauses = new ArrayList<>();

        if (userDtoFromWeb.getUsername() != null) {
            updateClauses.add("username = ?");
            parameters.add(userDtoFromWeb.getUsername());
        }
        if (userDtoFromWeb.getEmail() != null) {
            updateClauses.add("email = ?");
            parameters.add(userDtoFromWeb.getEmail());
        }
        if (userDtoFromWeb.getProfilePicture() != null) {
            updateClauses.add("profile_picture = ?");
            parameters.add(userDtoFromWeb.getProfilePicture());
        }
        if (userDtoFromWeb.getBio() != null) {
            updateClauses.add("bio = ?");
            parameters.add(userDtoFromWeb.getBio());
        }
        if (userDtoFromWeb.getImage() != null) {
            updateClauses.add("image_url = ?");
            parameters.add(USERS_ENT_DESKTOP_PICTURES + userDtoFromWeb.getImage().getSubmittedFileName());
        }
        if (userDtoFromWeb.getIsActive() != null) {
            updateClauses.add("is_active = ?");
            parameters.add(userDtoFromWeb.getIsActive());
        }
        if (userDtoFromWeb.getPassword() != null) {
            updateClauses.add("password = ?");
            parameters.add(userDtoFromWeb.getPassword());
        }
        if (userDtoFromWeb.getRole() != null) {
            updateClauses.add("role_id = ?");
            parameters.add(userDtoFromWeb.getRole().getRoleId());
        }
        if (userDtoFromWeb.getGender() != null) {
            updateClauses.add("gender_id = ?");
            parameters.add(userDtoFromWeb.getGender().getGenderId());
        }
        if (userDtoFromWeb.getBirthday() != null) {
            updateClauses.add("birthday = ?");
            parameters.add(Timestamp.valueOf(userDtoFromWeb.getBirthday().atStartOfDay()));
        }
        if (userDtoFromWeb.getName() != null) {
            updateClauses.add("name = ?");
            parameters.add(userDtoFromWeb.getName());
        }

        if (updateClauses.isEmpty()) {
            throw new DaoException("No fields to update.");
        }

        sqlBuilder.append(String.join(", ", updateClauses))
                .append(WHERE_ID);
        parameters.add(userDtoFromWeb.getId());

        String updateFilterSql = sqlBuilder.toString();

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(updateFilterSql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            preparedStatement.executeUpdate();
            return new User(userDtoFromWeb.getId());
        } catch (SQLException e) {
            throw new DaoException("Error updating user with ID " + userDtoFromWeb.getId() + ": " + e.getMessage(), e);
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

    public List<Long> findNotFollowers(Long userId) {


        List<Long> unsubscribedUserIds = new ArrayList<>();

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_NOT_FOLLOWERS)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    unsubscribedUserIds.add(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding followers with ID " + userId + ": " + e.getMessage(), e);
        }

        return unsubscribedUserIds;
    }

    public List<Long> findSubscriptions(Long userId) {
        List<Long> subscribedUserIds = new ArrayList<>();

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_SUBSCRIPTIONS)) {

            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subscribedUserIds.add(resultSet.getLong("user_id"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error finding subscriptions with ID " + userId + ": " + e.getMessage(), e);
        }

        return subscribedUserIds;
    }

    public void restore(Long id) {
        try (
                Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(RESTORE_USER)) {
            preparedStatement.setLong(1, id);
            int changedData = preparedStatement.executeUpdate();
            if (changedData == 0) throw new DaoException("No user to restore with ID " + id);
        } catch (SQLException e) {
            throw new DaoException("Error restoring user with ID " + id + ": " + e.getMessage(), e);
        }
    }



    public User buildUser(ResultSet resultSet) {
        try {
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("name"),
                    resultSet.getString("profile_picture"),
                    resultSet.getString("bio"),
                    resultSet.getString("image_url"),
                    resultSet.getBoolean("is_active"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.findById(resultSet.getInt("role_id")).orElseThrow(() -> new DaoException("Role not found")),
                    Gender.findByGenderId(resultSet.getInt("gender_id")).orElseThrow(() -> new DaoException("Gender not found")),
                    resultSet.getTimestamp("birthday").toLocalDateTime().toLocalDate());
        } catch (SQLException e) {
            throw new DaoException("Error creating user: " + e.getMessage(), e);
        }
    }

    private void setUser(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getProfilePicture());
        preparedStatement.setString(3, user.getBio());
        preparedStatement.setString(4, user.getImage());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getPassword());
        preparedStatement.setInt(7, user.getGender().getGenderId());
        preparedStatement.setTimestamp(8, Timestamp.valueOf(user.getBirthday().atStartOfDay()));
        preparedStatement.setString(9, user.getUsername());
        preparedStatement.setString(10, user.getRole().name());
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