package com.photogram.dao;

import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Subscription;
import com.photogram.exceptions.DaoException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionDao implements SubscriptionDaoInterface<Subscription, Long> {

    private static final UserDao USER_DAO = UserDao.getUserDaoAtomicReference();
    private static final AtomicReference<SubscriptionDao> SUBSCRIPTION_DAO_ATOMIC_REFERENCE = new AtomicReference<>();

    public static SubscriptionDao getSubscriptionDaoAtomicReference() {
        SUBSCRIPTION_DAO_ATOMIC_REFERENCE.get();
        if (SUBSCRIPTION_DAO_ATOMIC_REFERENCE.get() == null) {
            SubscriptionDao newSubscriptionDao = new SubscriptionDao();
            if (SUBSCRIPTION_DAO_ATOMIC_REFERENCE.compareAndSet(null, newSubscriptionDao)) {
                return newSubscriptionDao;
            } else {
                return SUBSCRIPTION_DAO_ATOMIC_REFERENCE.get();
            }
        }
        return SUBSCRIPTION_DAO_ATOMIC_REFERENCE.get();
    }

    public static final String FIND_SUBSCRIPTIONS = """
            SELECT f.user_id, f.follower_id, f.followed_at
            FROM followers f
            WHERE f.user_id =?
            """;


    public static final String FIND_SUBSCRIBERS = """
            SELECT f.user_id, f.follower_id, f.followed_at
            FROM followers f
            WHERE f.follower_id =?
            """;


    public static final String INSERT_NEW_SUBSCRIPTION = """
            INSERT INTO followers (user_id, follower_id, followed_at) VALUES (?, ?, ?)
            """;

    public static final String UPDATE_SUBSCRIPTION = """
            UPDATE followers SET followed_at = ? WHERE user_id = ? AND follower_id = ?
            """;


    public static final String DELETE_SUBSCRIPTION = """
            DELETE FROM followers WHERE user_id = ? AND follower_id = ?
            """;


    @Override
    public void delete(Long id) {

    }

    public void delete(Long userId, Long followerId) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBSCRIPTION)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, followerId);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate != 1) throw new DaoException("Failed to delete subscription");
        } catch (SQLException e) {
            throw new DaoException("Failed to delete subscription", e);
        }
    }


    @Override
    public void save(Subscription subscription) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(INSERT_NEW_SUBSCRIPTION)) {
            int affectedRows = setInfoToSubscription(subscription, preparedStatement);
            if (affectedRows == 0) throw new DaoException("Failed to insert a subscription");

        } catch (SQLException e) {
            throw new DaoException("Failed to insert new subscription", e);
        }
    }

    @Override
    public Subscription update(Subscription subscription) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SUBSCRIPTION)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(subscription.getSubscriptionDate().atStartOfDay()));
            preparedStatement.setLong(2, subscription.getUserId());
            preparedStatement.setLong(3, subscription.getFollowerUser());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) throw new DaoException("Failed to update a subscription");
            return subscription;
        } catch (SQLException e) {
            throw new DaoException("Failed to update subscription", e);
        }
    }


    @Override
    public List<Subscription> findSubscribers(Long userId) {
        List<Subscription> followers = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_SUBSCRIBERS)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Subscription subscription = Subscription.builder()
                            .userId(resultSet.getLong("user_id"))
                            .followerUser(resultSet.getLong("follower_id"))
                            .subscriptionDate(resultSet.getDate("followed_at").toLocalDate())
                            .build();
                    followers.add(subscription);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find followers users", e);
        }
        return followers;
    }

    @Override
    public List<Subscription> findSubscriptions(Long userId) {
        List<Subscription> subscriptions = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_SUBSCRIPTIONS)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Subscription subscription = Subscription.builder()
                            .userId(resultSet.getLong("user_id"))
                            .followerUser(resultSet.getLong("follower_id"))
                            .subscriptionDate(resultSet.getDate("followed_at").toLocalDate())
                            .build();
                    subscriptions.add(subscription);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find subscriptions", e);
        }
        return subscriptions;
    }






    private int setInfoToSubscription(Subscription subscription, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setLong(1, subscription.getUserId());
            preparedStatement.setLong(2, subscription.getFollowerUser());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(subscription.getSubscriptionDate().atStartOfDay()));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Failed to insert new subscription", e);
        }
    }

}
