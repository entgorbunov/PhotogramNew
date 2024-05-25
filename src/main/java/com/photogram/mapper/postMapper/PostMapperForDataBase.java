package com.photogram.mapper.postMapper;

import com.photogram.dto.postDto.PostDtoFromDataBase;
import com.photogram.entity.Post;
import com.photogram.entity.User;
import com.photogram.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapperForDataBase  implements Mapper<Post, PostDtoFromDataBase> {

    private static final AtomicReference<PostMapperForDataBase> POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE = new AtomicReference<>();

    public static PostMapperForDataBase getPostMapperForDataBaseAtomicReference() {
        POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
        if (POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.get() == null) {
            PostMapperForDataBase newPostMapperForDataBase = new PostMapperForDataBase();
            if (POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.compareAndSet(null, newPostMapperForDataBase)) {
                return newPostMapperForDataBase;
            } else {
                return POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
            }
        }
        return POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
    }

    @Override
    public PostDtoFromDataBase toDto(Post post) {
        return PostDtoFromDataBase.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .caption(post.getCaption())
                .postTime(post.getPostTime())
                .imageUrl(post.getImageUrl())
                .text(post.getText())
                .isDeleted(post.getIsDeleted())
                .build();
    }
    @Override
    public Post toEntity(PostDtoFromDataBase postDtoFromDataBase) {
        return null;
    }

    public static Post toEntity(PostDtoFromDataBase postDtoFromDataBase, User user) {
        return Post.builder()
                .user(user)
                .caption(postDtoFromDataBase.getCaption())
                .postTime(postDtoFromDataBase.getPostTime())
                .imageUrl(postDtoFromDataBase.getImageUrl())
                .text(postDtoFromDataBase.getText())
                .isDeleted(postDtoFromDataBase.getIsDeleted())
                .build();
    }


}
