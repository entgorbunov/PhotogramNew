package com.photogram.service.userService;

import com.photogram.dao.UserDao;
import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.User;
import com.photogram.exceptions.ServiceException;
import com.photogram.exceptions.ValidationException;
import com.photogram.mapper.userMapper.UserMapperForWeb;
import com.photogram.service.ImageService;
import com.photogram.service.UserServiceForWebInterface;
import com.photogram.validator.CreateUserValidator;
import com.photogram.validator.ValidationResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceForWeb implements UserServiceForWebInterface<UserDtoFromWeb, Long> {
    private static final CreateUserValidator CREATE_USER_VALIDATOR = CreateUserValidator.getCreateUserValidatorAtomicReference();
    private static final UserDao USER_DAO_ATOMIC_REFERENCE = UserDao.getUserDaoAtomicReference();
    private static final UserMapperForWeb USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE = UserMapperForWeb.getUserMapperForWebAtomicReference();
    private static final String IMAGE_FOLDER = "users/";
    private static final AtomicReference<UserServiceForWeb> USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE = new AtomicReference<>();
    private static final ImageService IMAGE_SERVICE_ATOMIC_REFERENCE = ImageService.getImageServiceAtomicReference();

    public static UserServiceForWeb getUserServiceForWebAtomicReference() {
        USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get();
        if (USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get() == null) {
            UserServiceForWeb userServiceForWeb = new UserServiceForWeb();
            if (USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.compareAndSet(null, userServiceForWeb)) {
                return userServiceForWeb;
            } else {
                return USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get();
            }
        }
        return USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get();
    }

    @Override
    public Long create(UserDtoFromWeb userDtoFromWeb) {
        ValidationResult validationResult = CREATE_USER_VALIDATOR.isValid(userDtoFromWeb);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        try {

            IMAGE_SERVICE_ATOMIC_REFERENCE.upload(IMAGE_FOLDER + userDtoFromWeb.getImage().getSubmittedFileName(), userDtoFromWeb.getImage().getInputStream());
            User userEntity = USER_MAPPER_FOR_WEB_ATOMIC_REFERENCE.toEntity(userDtoFromWeb);
            USER_DAO_ATOMIC_REFERENCE.save(userEntity);
            return userEntity.getId();
        } catch (IOException e) {
            throw new ServiceException("Error uploading the image", e);
        }

    }


    @Override
    public UserDtoFromWeb updateWithImage(UserDtoFromWeb userDtoFromWeb) {
        Optional<User> optionalUser = USER_DAO_ATOMIC_REFERENCE.findById(userDtoFromWeb.getId());
        if (optionalUser.isEmpty()) {
            throw new ServiceException("User not found with the provided ID");
        }

        try {

            IMAGE_SERVICE_ATOMIC_REFERENCE.upload(IMAGE_FOLDER + userDtoFromWeb.getImage().getSubmittedFileName(), userDtoFromWeb.getImage().getInputStream());
            USER_DAO_ATOMIC_REFERENCE.update(userDtoFromWeb);
            return userDtoFromWeb;

        } catch (IOException e) {
            throw new ServiceException("Image upload failed");
        }


    }

    @Override
    public UserDtoFromWeb findById(Long aLong) {
        return null;
    }


    public UserDtoFromWeb update(UserDtoFromWeb userDtoFromWeb) {
        Optional<User> optionalUser = USER_DAO_ATOMIC_REFERENCE.findById(userDtoFromWeb.getId());
        if (optionalUser.isEmpty()) {
            throw new ServiceException("User not found with the provided ID");
        }

        USER_DAO_ATOMIC_REFERENCE.update(userDtoFromWeb);
        return userDtoFromWeb;

    }


    @Override
    public List<UserDtoFromWeb> findAll() {
        return List.of();
    }

    public UserDtoFromWeb findByUserId(Long id) {
        return UserDtoFromWeb.builder().build();
    }


    @Override
    public void delete(Long userId) {
        throw new ServiceException("Do not use this method");
    }


    public Long changePassword(Long userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new ServiceException("New password must not be empty");
        }

        Optional<User> optionalUser = USER_DAO_ATOMIC_REFERENCE.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ServiceException("User not found with the provided ID");
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(oldPassword)) {
            throw new ServiceException("Old password does not match");
        }

        UserDtoFromWeb dtoFromWeb = UserDtoFromWeb.builder()
                .id(user.getId())
                .password(newPassword)
                .build();

        USER_DAO_ATOMIC_REFERENCE.update(dtoFromWeb);
        return dtoFromWeb.getId();
    }

}
