package com.photogram.mapper.postMapper;

import com.photogram.dao.UserDao;
import com.photogram.dto.postDto.PostDtoFromWeb;
import com.photogram.entity.Post;
import com.photogram.exceptions.ServiceException;
import com.photogram.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapperForWeb implements Mapper<Post, PostDtoFromWeb> {
    public static final AtomicReference<PostMapperForWeb> POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE = new AtomicReference<>();

    public static PostMapperForWeb getPostMapperForWebAtomicReference() {
        POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get();
        if (POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get() == null) {
            PostMapperForWeb postMapperForWeb = new PostMapperForWeb();
            if (POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE.compareAndSet(null, postMapperForWeb)) {
                return postMapperForWeb;
            } else {
                return POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get();
            }
        }
        return POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE.get();
    }

    private static final UserDao userDao = UserDao.getUserDaoAtomicReference();

    @Override
    public PostDtoFromWeb toDto(Post entity) {
        return null;
    }

    @Override
    public Post toEntity(PostDtoFromWeb postDtoFromWeb) {
        Post.PostBuilder builder = Post.builder()
                .id(postDtoFromWeb.getId())
                .caption(postDtoFromWeb.getCaption())
                .imageUrl("/Users/ent/Desktop/Pictures/posts/" + postDtoFromWeb.getImage().getSubmittedFileName())
                .text(postDtoFromWeb.getText())
                .postTime(postDtoFromWeb.getPostTime())
                .user(userDao.findById(postDtoFromWeb.getUserId()).orElseThrow(() -> new ServiceException("User Not Found")));
                return builder.build();
    }
}
