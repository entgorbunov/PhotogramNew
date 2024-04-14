package com.photogram.service;

import com.photogram.builder.UserBuilder;
import com.photogram.dao.UserDao;
import com.photogram.daoException.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.dto.PostDto;
import com.photogram.dto.UserDto;
import com.photogram.entity.User;
import com.photogram.mapper.PostMapper;
import com.photogram.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {

    public static final UserService INSTANCE = new UserService();
    private static final Long MAX_LONG = Long.MAX_VALUE;

    private final UserDao userDao = UserDao.getInstance();
    public static UserService getInstance() {
        return INSTANCE;
    }

    Connection connection = ConnectionManager.open();

    public List<UserDto> findAll() {
        return userDao.findAll(MAX_LONG, connection).stream()
                .map(UserMapper::userToUserDto)
                .toList();
    }

    public UserDto findById(Long id, Connection connection) {
        var optionalUser = userDao.findById(id, connection);
        return optionalUser.map(UserMapper::userToUserDto).orElseThrow(() -> new DaoException("Find the user with " +
                                                                                              "userID" + id +
                                                                                              " was " +
                                                                                              "failed"));
    }

    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.userDtoToUser(userDto);
        userDao.save(user, connection);
        return UserMapper.userToUserDto(user);
    }

    public UserDto updateUser(UserDto userDto) {
        User user = userDao.findById(userDto.getId(), connection)
                .orElseThrow(() -> new DaoException("User isn't found while updating the user"));
        user.setUsername(userDto.getUsername());
        user.setBio(userDto.getBio());
        userDao.update(user, connection);
        return userDto;
    }

    public boolean deleteUser(Long userId) {
        boolean deleted = userDao.delete(userId, connection);
        if (!deleted) throw new DaoException("No user to delete with ID " + userId);
        return true;
    }









}
