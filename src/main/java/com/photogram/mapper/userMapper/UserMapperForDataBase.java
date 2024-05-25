package com.photogram.mapper.userMapper;

import com.photogram.dao.PostDao;
import com.photogram.dao.SubscriptionDao;
import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.entity.User;
import com.photogram.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapperForDataBase  implements Mapper<User, UserDtoFromDataBase> {
    public static final String IMAGE_FOLDER = "/Users/ent/Desktop/Pictures/users/";

    private static final PostDao POST_DAO = PostDao.getPostDaoAtomicReference();
    private static final SubscriptionDao SUBSCRIPTION_DAO = SubscriptionDao.getSubscriptionDaoAtomicReference();
    private static final AtomicReference<UserMapperForDataBase> USER_MAPPER_FOR_DATA_BASE = new AtomicReference<>();

    public static UserMapperForDataBase getUserMapperForDataBase() {
        USER_MAPPER_FOR_DATA_BASE.get();
        if (USER_MAPPER_FOR_DATA_BASE.get() == null) {
            UserMapperForDataBase userMapperForDataBase = new UserMapperForDataBase();
            if (USER_MAPPER_FOR_DATA_BASE.compareAndSet(null, userMapperForDataBase)) {
                return userMapperForDataBase;
            } else {
                return USER_MAPPER_FOR_DATA_BASE.get();
            }
        }
        return USER_MAPPER_FOR_DATA_BASE.get();
    }


    public UserDtoFromDataBase toDto(User user) {
        return UserDtoFromDataBase.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .imageUrl(IMAGE_FOLDER + user.getImage())
                .birthday(String.valueOf(user.getBirthday()))
                .subscribers(SUBSCRIPTION_DAO.findSubscribers(user.getId()))
                .subscriptions(SUBSCRIPTION_DAO.findSubscriptions(user.getId()))
                .posts(POST_DAO.findAllById(user.getId()))
                .role(user.getRole())
                .build();
    }

    @Override
    public User toEntity(UserDtoFromDataBase dto) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .name(dto.getName())
                .profilePicture(dto.getProfilePicture())
                .bio(dto.getBio())
                .image(dto.getImageUrl())
                .isActive(dto.getIsActive())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .gender(dto.getGender())
                .build();
    }


}
