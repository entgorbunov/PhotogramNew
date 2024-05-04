package com.photogram.service;

import com.photogram.dao.UserDao;
import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.User;
import com.photogram.exceptions.ServiceException;
import com.photogram.exceptions.ValidationException;
import com.photogram.mapper.UserMapperForWeb;
import com.photogram.validator.CreateUserValidator;
import com.photogram.validator.ValidationResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceForWeb implements UserServiceForWebInterface<UserDtoFromWeb, Long> {
    private final CreateUserValidator createUserValidator = CreateUserValidator.getINSTANCE();
    private final UserDao userDao = UserDao.getInstance();
    private final UserMapperForWeb userMapperForWeb = UserMapperForWeb.getINSTANCE();
    private final ImageService imageService = ImageService.getINSTANCE();
    private static final String IMAGE_FOLDER = "users/";
    public static volatile UserServiceForWeb instance = new UserServiceForWeb();

    public static UserServiceForWeb getInstance() {
        if (instance == null) {
            synchronized (UserServiceForWeb.class) {
                if (instance == null) {
                    instance = new UserServiceForWeb();
                }
            }
        }
        return instance;
    }

    @Override
    public Long create(UserDtoFromWeb userDtoFromWeb)  {
        ValidationResult validationResult = createUserValidator.isValid(userDtoFromWeb);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        try {
            imageService.upload(IMAGE_FOLDER + userDtoFromWeb.getImage().getSubmittedFileName(), userDtoFromWeb.getImage().getInputStream() );
            User userEntity = userMapperForWeb.toEntity(userDtoFromWeb);
            userDao.save(userEntity);
            return userEntity.getId();
        } catch (IOException e) {
            throw new ServiceException("Error uploading the image", e);
        }

    }

    @Override
    public UserDtoFromWeb update(UserDtoFromWeb userDtoFromWeb) {
        return UserDtoFromWeb.builder().build();
    }

    @Override
    public List<UserDtoFromWeb> findAll() {
        return List.of();
    }

    @Override
    public UserDtoFromWeb findById(Long id) {
        return UserDtoFromWeb.builder().build();
    }


    @Override
    public void delete(Long userId) {
        throw new ServiceException("Do not use this method");
    }




}
