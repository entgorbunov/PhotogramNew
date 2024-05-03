package com.photogram.service;

import com.photogram.dao.UserDao;
import com.photogram.dto.UserDto;
import com.photogram.entity.User;
import com.photogram.exceptions.DaoException;
import com.photogram.exceptions.ValidationException;
import com.photogram.mapper.UserMapper;
import com.photogram.validator.CreateUserValidator;
import com.photogram.validator.ValidationResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService implements UserServiceInterface<UserDto, Long> {
    private final CreateUserValidator createUserValidator = CreateUserValidator.getINSTANCE();
    private final UserDao userDao = UserDao.getInstance();
    private final UserMapper userMapper = UserMapper.getINSTANCE();
    private final ImageService imageService = ImageService.getINSTANCE();

    public static volatile UserService instance = new UserService();

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
    public Long create(UserDto userDto) {
        ValidationResult validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User userEntity = userMapper.toEntity(userDto);
        userDao.save(userEntity);
        return userEntity.getId();
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return userDao.findById(id).map(userMapper::toDto).orElseThrow(() ->
                new DaoException("Find the user with userID" + id + " was failed"));
    }


    @Override
    public UserDto update(UserDto userDto) {
        Optional<User> optionalUser = userDao.findById(userDto.getId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with provided email and password");
        }
        User user = optionalUser.get();
        return userMapper.toDto(user);

    }

    @Override
    public void delete(Long userId) {
        userDao.delete(userId);
    }

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::toDto);
    }


}
