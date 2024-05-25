package com.photogram.dao;

import com.photogram.dataSource.ConnectionManager;
import com.photogram.entity.Image;
import com.photogram.exceptions.DaoException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDao implements ImageDaoInterface<Image, Long> {
    private final static AtomicReference<ImageDao> IMAGE_DAO_ATOMIC_REFERENCE = new AtomicReference<>();

    public static ImageDao getImageDaoAtomicReference() {
        IMAGE_DAO_ATOMIC_REFERENCE.get();
        if (IMAGE_DAO_ATOMIC_REFERENCE.get() == null) {
            ImageDao newImageDao = new ImageDao();
            if (IMAGE_DAO_ATOMIC_REFERENCE.compareAndSet(null, newImageDao)) {
                return newImageDao;
            } else {
                return IMAGE_DAO_ATOMIC_REFERENCE.get();
            }
        }
        return IMAGE_DAO_ATOMIC_REFERENCE.get();
    }



    private static final String FIND_ALL_SQL = """
            SELECT id,
            path,
            post_id,
            user_id,
            is_deleted,
            uploaded_time
            FROM images
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String INSERT_NEW_IMAGE = """
            INSERT INTO images (id, path, post_id, user_id, is_deleted, uploaded_time) VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_IMAGE = """
            UPDATE images SET id = ?, path = ?, post_id = ?,  user_id = ?, is_deleted = ?, uploaded_time = ? WHERE id = ?
            """;

    private static final String SOFT_DELETE_IMAGE = """
            UPDATE images set is_deleted = TRUE where id = ?
            """;

    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SOFT_DELETE_IMAGE)) {
            preparedStatement.setLong(1, id);
            int changedData = preparedStatement.executeUpdate();
            if (changedData == 0) throw new DaoException("Error deleting image");
        } catch (SQLException e) {
            throw new DaoException("Error while deleting image", e);
        }
    }

    @Override
    public void save(Image image) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_IMAGE, Statement.RETURN_GENERATED_KEYS)) {
            var affectedRows = setInfoToImage(image, preparedStatement);
            if (affectedRows == 0) {
                throw new DaoException("Creating comment failed");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    image.setId(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("Creating image failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error while saving image", e);
        }
    }


    @Override
    public Image update(Image image) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_IMAGE)) {
            var affectedRows = setInfoToImage(image, preparedStatement);
            if (affectedRows == 0) {
                throw new DaoException("Updating post failed");
            }
            return image;
        } catch (SQLException e) {
            throw new DaoException("Error while updating image", e);
        }
    }

    @Override
    public Optional<Image> findById(Long id) {
        Image image = new Image();
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createImage(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error while finding image", e);
        }
        return Optional.of(image);
    }

    @Override
    public List<Image> findAll() {
        List<Image> images = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)) {
            while (resultSet.next()) {
                images.add(createImage(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException("Error while finding images", e);
        }
        return images;
    }

    private Image createImage(ResultSet resultSet) {
        try {
            return new Image(resultSet.getLong("id"),
                    resultSet.getString("path"),
                    PostDao.getPostDaoAtomicReference().findById(resultSet.getLong("post_id")).orElseThrow(() ->
                            new DaoException("Post hasn't found by Id")),
                    UserDao.getUserDaoAtomicReference().findById(resultSet.getLong("user_id")).orElseThrow(() ->
                            new DaoException("User hasn't found by Id")),
                    resultSet.getBoolean("is_deleted"),
                    resultSet.getTimestamp("uploaded_time").toLocalDateTime());
        } catch (SQLException e) {
            throw new DaoException("Error while creating image", e);
        }
    }

    private static int setInfoToImage(Image image, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, image.getId());
        preparedStatement.setObject(2, image.getPath());
        preparedStatement.setObject(3, image.getPostId().getId());
        preparedStatement.setObject(4, image.getUserId().getId());
        preparedStatement.setObject(5, image.getIsDeleted());
        preparedStatement.setObject(6, image.getUploadedTime());
        int executeUpdate = preparedStatement.executeUpdate();
        if (executeUpdate == 0) throw new DaoException("Error setting info into image");
        return executeUpdate;
    }
}
