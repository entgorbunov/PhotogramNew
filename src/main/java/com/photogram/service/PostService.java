package com.photogram.service;

import com.photogram.dao.PostDao;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.dto.PostDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostService {
    public static final PostService INSTANCE = getInstance();

    private final PostDao postDao = PostDao.getInstance();

    Connection connection = ConnectionManager.open();

    public static PostService getInstance() {
        return INSTANCE;
    }

    public List<PostDto> findAllByUserId(Long userId) {
        return postDao.findAllByUserId(userId, connection).stream()
                .map(post -> PostDto.builder()
                        .id(post.getId())
                        .userId(post.getUser().getId())
                        .caption(post.getCaption())
                        .postTime(post.getPostTime())
                        .imageUrl(post.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }


}




