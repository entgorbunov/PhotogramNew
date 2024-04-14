package com.photogram.service;

import com.photogram.dao.UserDao;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.dto.UserDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {

    public static final UserService INSTANCE = new UserService();

    private final UserDao userDao = UserDao.getInstance();
    Connection connection = ConnectionManager.open();

    public List<UserDto> findAll() {
        return userDao.findAll(connection).stream()
                .map(user -> UserDto.builder().
                        id(user.getId()).
                        bio(user.getBio())
                        .build())
                .collect(Collectors.toList());
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
