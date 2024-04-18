package com.photogram.mapper;

import com.photogram.dto.PostDto;
import com.photogram.entity.Post;
import com.photogram.entity.User;

public class PostMapper {
    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .caption(post.getCaption())
                .postTime(post.getPostTime())
                .imageUrl(post.getImageUrl())
                .build();
    }

    public static Post toPost(PostDto postDto, User user) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setUser(user);
        post.setCaption(postDto.getCaption());
        post.setPostTime(postDto.getPostTime());
        post.setImageUrl(postDto.getImageUrl());
        return post;
    }
}
