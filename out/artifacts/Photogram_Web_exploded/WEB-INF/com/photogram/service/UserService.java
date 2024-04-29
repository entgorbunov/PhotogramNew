package com.photogram.service;

import com.photogram.exceptions.DaoException;
import com.photogram.exceptions.ServiceException;
import com.photogram.exceptions.ValidationException;
import com.photogram.dao.UserDao;
import com.photogram.dto.CreateUserDto;
import com.photogram.dto.UserDto;
import com.photogram.entity.User;
import com.photogram.mapper.CreateUserMapper;
import com.photogram.mapper.UserMapper;
import com.photogram.validator.CreateUserValidator;
import com.photogram.validator.ValidationResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService implements UserServiceInterface<UserDto, Long> {
    private final CreateUserValidator createUserValidator = CreateUserValidator.getINSTANCE();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getINSTANCE();
    private final ImageService imageService = ImageService.getINSTANCE();

    public static volatile UserService instance = new UserService();

    public Long create(CreateUserDto userDto) {
        ValidationResult validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User userEntity = createUserMapper.mapFrom(userDto);
        try {
            imageService.upload( userEntity.getImage(), userDto.getImage().getInputStream());
        } catch (IOException e) {
            throw new ServiceException("Could not upload image", e);
        }
        userDao.save(userEntity);


        return userEntity.getId();
    }

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
