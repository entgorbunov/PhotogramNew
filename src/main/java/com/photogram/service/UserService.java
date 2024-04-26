package com.photogram.service;

import com.photogram.dao.UserDao;
import com.photogram.daoException.DaoException;
import com.photogram.dto.UserDto;
import com.photogram.entity.User;
import com.photogram.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService implements UserServiceInterface<UserDto, Long> {

    public static volatile UserService instance = new UserService();

    private final UserDao userDao = UserDao.getInstance();

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll().stream()
                .map(UserMapper::userToUserDto)
                .toList();
    }

    @Override
    public UserDto findByUserId(Long id) {
        return userDao.findById(id).map(UserMapper::userToUserDto).orElseThrow(() ->
                new DaoException("Find the user with userID" + id + " was failed"));
    }

    @Override
    public void create(UserDto userDto) {
        User user = UserMapper.userDtoToUser(userDto);
        userDao.save(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userDao.findById(userDto.getId())
                .orElseThrow(() ->
                        new DaoException("User isn't found while updating the user"));
        user.setUsername(userDto.getUsername());
        user.setBio(userDto.getBio());
        userDao.update(user);
        return userDto;
    }

    @Override
    public void delete(Long userId) {
        userDao.delete(userId);
    }


}
