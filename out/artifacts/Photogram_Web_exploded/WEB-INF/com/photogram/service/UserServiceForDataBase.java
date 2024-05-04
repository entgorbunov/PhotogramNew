package com.photogram.service;

import com.photogram.dao.UserDao;
import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.entity.User;
import com.photogram.exceptions.ServiceException;
import com.photogram.mapper.UserMapperForDataBase;

import java.util.List;
import java.util.Optional;

public class UserServiceForDataBase implements UserServiceForDataBaseInterface<UserDtoFromDataBase, Long>{

    private final UserDao USER_DAO = UserDao.getInstance();
    private final UserMapperForDataBase USER_MAPPER_FOR_DATA_BASE = UserMapperForDataBase.getINSTANCE();

    public static volatile UserServiceForDataBase instance = new UserServiceForDataBase();

    public static UserServiceForDataBase getInstance() {
        if (instance == null) {
            synchronized (UserServiceForDataBase.class) {
                if (instance == null) {
                    instance = new UserServiceForDataBase();
                }
            }
        }
        return instance;
    }

    @Override
    public void delete(Long userId) {
        USER_DAO.delete(userId);
    }

    @Override
    public Long create(UserDtoFromDataBase userDtoFromDataBase) {
        return 0L;
    }

    @Override
    public UserDtoFromDataBase update(UserDtoFromDataBase userDtoFromDataBase) {
        Optional<User> optionalUser = USER_DAO.findById(userDtoFromDataBase.getId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with provided email and password");
        }
        User user = optionalUser.get();
        return USER_MAPPER_FOR_DATA_BASE.toDto(user);
    }

    @Override
    public UserDtoFromDataBase findById(Long userId) {
        return USER_DAO.findById(userId)
                .map(USER_MAPPER_FOR_DATA_BASE::toDto)
                .orElseThrow(() -> new ServiceException("User with ID " + userId + " not found"));
    }

    @Override
    public List<UserDtoFromDataBase> findAll() {
        return USER_DAO.findAll().stream()
                .map(USER_MAPPER_FOR_DATA_BASE::toDto)
                .toList();

    }

    public Optional<UserDtoFromDataBase> login(String email, String password) {
        return USER_DAO.findByEmailAndPassword(email, password)
                .map(USER_MAPPER_FOR_DATA_BASE::toDto);
    }
}
