package com.photogram.dao;

import com.photogram.exceptions.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Subscription;
import com.photogram.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionDao implements SubscriptionDaoInterface<Subscription, Long> {
    @Getter
    private static final SubscriptionDao INSTANCE = new SubscriptionDao();



    public static final String FIND_FOLLOWING = """
            SELECT u.id, username, profile_picture, bio, is_private, image_url from users u
            INNER JOIN subscriptions s ON u.id = s.following_id WHERE s.follower_id = ? AND u.is_active = TRUE
            """;

    public static final String FIND_FOLLOWERS = """
            SELECT u.id, username, profile_picture, bio, is_private, image_url FROM users u
            INNER JOIN subscriptions s ON u.id = s.follower_id WHERE s.following_id = ? AND u.is_active = TRUE
            """;

    public static final String INSERT_NEW_SUBSCRIPTION = """
            INSERT INTO subscriptions (id, follower_id, following_id, subscription_time) VALUES (?, ?, ?, ?)
            """;
    public static final String UPDATE_SUBSCRIPTION = """
            UPDATE subscriptions SET follower_id = ?, following_id = ?, subscription_time = ? WHERE id = ?
            """;
    public static final String DELETE_SUBSCRIPTION = """
            DELETE FROM subscriptions WHERE id = ?
            """;

    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBSCRIPTION)) {
            preparedStatement.setLong(1, id);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate != 1) throw new DaoException("Failed to delete subscription");
        } catch (SQLException e) {
            throw new DaoException("Failed to delete subscription", e);
        }
    }

    @Override
    public void save(Subscription subscription) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(INSERT_NEW_SUBSCRIPTION);) {
            int affectedRows = setInfoToSubscription(subscription, preparedStatement);
            if (affectedRows == 0) throw new DaoException("Failed to insert a subscription");
        } catch (SQLException e) {
            throw new DaoException("Failed to insert new subscription", e);
        }
    }

    @Override
    public Subscription update(Subscription subscription) {
        try (Connection connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(UPDATE_SUBSCRIPTION)) {
            int affectedRows = setInfoToSubscription(subscription, preparedStatement);
            if (affectedRows == 0) throw new DaoException("Failed to update a subscription");
            return subscription;
        } catch (SQLException e) {
            throw new DaoException("Failed to update subscription", e);
        }
    }



    @Override
    public List<User> findSubscriptions(Long userId) {
        List<User> following = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_FOLLOWING)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    following.add(UserDao.getInstance().buildUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find following users", e);
        }
        return following;
    }

    @Override
    public List<User> findSubscribers(Long userId) {
        List<User> followers = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_FOLLOWERS)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    followers.add(UserDao.getInstance().buildUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find followers users", e);
        }
        return followers;
    }


    private int setInfoToSubscription(Subscription subscription, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setLong(1, subscription.getId());
            preparedStatement.setLong(2, subscription.getSubscriberUser().getId());
            preparedStatement.setLong(3, subscription.getSubscriptionUser().getId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(subscription.getSubscriptionDate()));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to insert new subscription", e);
        }
    }

}
