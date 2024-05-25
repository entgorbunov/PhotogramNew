package com.photogram.service.userService;

import com.photogram.dao.PostDao;
import com.photogram.dao.UserDao;
import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Post;
import com.photogram.entity.User;
import com.photogram.exceptions.ServiceException;
import com.photogram.mapper.userMapper.UserMapperForDataBase;
import com.photogram.service.ImageService;
import com.photogram.service.UserServiceForDataBaseInterface;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceForDataBase implements UserServiceForDataBaseInterface<UserDtoFromDataBase, Long> {
    private static final String IMAGE_FOLDER = "users/";

    private static final ImageService IMAGE_SERVICE = ImageService.getImageServiceAtomicReference();
    private static final UserDao USER_DAO_ATOMIC_REFERENCE = UserDao.getUserDaoAtomicReference();
    private static final PostDao POST_DAO_ATOMIC_REFERENCE = PostDao.getPostDaoAtomicReference();
    private static final UserMapperForDataBase USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE = UserMapperForDataBase.getUserMapperForDataBase();

    private static final AtomicReference<UserServiceForDataBase> USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE = new AtomicReference<>();

    public static UserServiceForDataBase getUserServiceForDataBaseAtomicReference() {
        USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
        if (USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get() == null) {
            UserServiceForDataBase userServiceForDataBase = new UserServiceForDataBase();
            if (USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.compareAndSet(null, userServiceForDataBase)) {
                return userServiceForDataBase;
            } else {
                return USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
            }
        }
        return USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
    }

    @Override
    public void delete(Long userId) {
        USER_DAO_ATOMIC_REFERENCE.delete(userId);
    }

    @Override
    public Long create(UserDtoFromDataBase userDtoFromDataBase) {
        return 0L;
    }

    @Override
    public UserDtoFromDataBase updateWithImage(UserDtoFromDataBase userDtoFromDataBase) {
        Optional<User> optionalUser = USER_DAO_ATOMIC_REFERENCE.findById(userDtoFromDataBase.getId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with provided email and password");
        }
        User user = optionalUser.get();
        User update = USER_DAO_ATOMIC_REFERENCE.update(user);
        return USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(update);
    }

    @Override
    public UserDtoFromDataBase findById(Long aLong) {
        return null;
    }

    public UserDtoFromDataBase updateWithImage(UserDtoFromWeb userDtoFromWeb) {
        Optional<User> optionalUser = USER_DAO_ATOMIC_REFERENCE.findById(userDtoFromWeb.getId());
        if (optionalUser.isEmpty()) {
            throw new ServiceException("User not found with the provided ID");
        }

        try {
            IMAGE_SERVICE.upload(IMAGE_FOLDER + userDtoFromWeb.getImage().getSubmittedFileName(), userDtoFromWeb.getImage().getInputStream());
        } catch (IOException e) {
            throw new ServiceException("Image upload failed");
        }

        User updatedUser = USER_DAO_ATOMIC_REFERENCE.update(userDtoFromWeb);

        return USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(updatedUser);
    }

    public UserDtoFromDataBase update(UserDtoFromWeb userDtoFromWeb) {
        Optional<User> optionalUser = USER_DAO_ATOMIC_REFERENCE.findById(userDtoFromWeb.getId());
        if (optionalUser.isEmpty()) {
            throw new ServiceException("User not found with the provided ID");
        }

        User updatedUser = USER_DAO_ATOMIC_REFERENCE.update(userDtoFromWeb);

        return USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(updatedUser);
    }

    public List<UserDtoFromDataBase> getRecommendedUsers(Long currentUserId) {
        List<Long> unsubscribedUserIds = USER_DAO_ATOMIC_REFERENCE.findNotFollowers(currentUserId);

        Map<Long, Integer> postCounts = new HashMap<>();
        for (Long userId : unsubscribedUserIds) {
            List<Post> posts = POST_DAO_ATOMIC_REFERENCE.findAllById(userId);
            postCounts.put(userId, posts.size());
        }

        List<Long> sortedUserIds = unsubscribedUserIds.stream()
                .sorted((u1, u2) -> postCounts.get(u2).compareTo(postCounts.get(u1)))
                .collect(Collectors.toList());

        List<UserDtoFromDataBase> recommendedUsers = new ArrayList<>();
        for (Long userId : sortedUserIds) {
            User user = USER_DAO_ATOMIC_REFERENCE.findById(userId).orElseThrow(() -> new ServiceException("User not found with the provided ID"));
            UserDtoFromDataBase dto = USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(user);
            recommendedUsers.add(dto);
        }
        return recommendedUsers;
    }

    public List<UserDtoFromDataBase> getSubscriptions(Long currentUserId) {
        List<Long> subscribedUserIds = USER_DAO_ATOMIC_REFERENCE.findSubscriptions(currentUserId);

        List<UserDtoFromDataBase> subscriptions = new ArrayList<>();
        for (Long userId : subscribedUserIds) {
            User user = USER_DAO_ATOMIC_REFERENCE.findById(userId).orElseThrow(() -> new ServiceException("User not found with the provided ID"));
            UserDtoFromDataBase dto = USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(user);
            subscriptions.add(dto);
        }

        return subscriptions;
    }

    public UserDtoFromDataBase findByUserId(Long userId) {
        return USER_DAO_ATOMIC_REFERENCE.findById(userId)
                .map(USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE::toDto)
                .orElseThrow(() -> new ServiceException("User with ID " + userId + " not found"));
    }

    @Override
    public List<UserDtoFromDataBase> findAll() {
        return USER_DAO_ATOMIC_REFERENCE.findAll().stream()
                .map(USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE::toDto)
                .toList();

    }

    public Optional<UserDtoFromDataBase> login(String email, String password) {
        return USER_DAO_ATOMIC_REFERENCE.findByEmailAndPassword(email, password)
                .map(USER_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE::toDto);
    }


}
