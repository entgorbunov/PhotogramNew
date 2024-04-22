package com.photogram;


import com.photogram.builder.ImageBuilder;
import com.photogram.builder.PostBuilder;
import com.photogram.dao.ImageDao;
import com.photogram.dao.PostDao;
import com.photogram.dao.UserDao;
import com.photogram.dataSource.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;


import static com.photogram.builder.UserBuilder.userBuilder;

public class DaoRunner {
    public static void main(String[] args) {
        try (Connection connection = ConnectionManager.open()) {
            var userDao = UserDao.getInstance();
            var postDao = PostDao.getInstance();
            var imageDao = ImageDao.getInstance();


            var user = userBuilder(0L, "Anton", " ", " ", true, "null");
            var post = PostBuilder.postBuilder(0L, 8L, "...",
                    Timestamp.valueOf(
                            "2020-02-02 00:00:10").toLocalDateTime(), "nulldsa");
            var image = ImageBuilder.imageBuilder(0L, "null", 1L,
                    12L,
                    Timestamp.valueOf("2020-02-02 00:00:00").toLocalDateTime());


//            userDao.save(user, connection);
//            userDao.save(user, connection);
//            userDao.delete(11L, connection);
//            postDao.save(post, connection);
            postDao.update(post, connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
