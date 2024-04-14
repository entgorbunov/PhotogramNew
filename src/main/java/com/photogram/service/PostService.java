package com.photogram.service;

import com.photogram.dao.PostDao;
import com.photogram.dao.UserDao;
import com.photogram.daoException.DaoException;
import com.photogram.dataSource.ConnectionManager;
import com.photogram.dto.PostDto;
import com.photogram.entity.Post;
import com.photogram.mapper.PostMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostService {
    public static final PostService INSTANCE = new PostService();

    private final PostDao postDao = PostDao.getInstance();
    public static final UserDao userDao = UserDao.getInstance();
    private final UserService userService = UserService.getInstance();

    Connection connection = ConnectionManager.open();

    public static PostService getInstance() {
        return INSTANCE;
    }

    public List<PostDto> findAllPostsByUserId(Long userId) {
        var user = userDao.findById(userId, connection).orElse(null);
        if (user == null) return Collections.emptyList();
        return postDao.findAllByUserId(user.getId(), connection).stream()
                .map(PostMapper::toPostDto)
                .toList();
    }

    public PostDto findAnyPostByUserId(Long userId) {
        var user = userDao.findById(userId, connection).orElse(null);
        return postDao.findAllByUserId(user.getId(), connection).stream()
                .map(PostMapper::toPostDto).findAny().orElseThrow(() -> new DaoException("The Post was not " +
                                                                                         "found " +
                                                                                         "while finding"));

    }

    public PostDto createPost(PostDto postDto) {
        var user =
                userDao.findById(postDto.getUserId(), connection).orElseThrow(() -> new DaoException("User" + postDto.getUserId() + " not found while finding post"));

        var post = PostMapper.toPost(postDto, user);
        postDao.save(post, connection);
        return PostMapper.toPostDto(post);
    }

    public PostDto updatePost(PostDto postDto) {
        return postDao.findById(postDto.getId(), connection)
                .map(post -> {
                    postDao.update(post, connection);
                    return PostMapper.toPostDto(post);
                })
                .orElseThrow(() -> new DaoException("Post" + postDto.getId() + " not found while updating the " +
                                                    "post"));
    }

    public boolean deletePost(Long postId) {
        boolean deleted = postDao.delete(postId, connection);
        if (!deleted) throw new DaoException("No post to delete with ID " + postId);
        return true;
    }


}




