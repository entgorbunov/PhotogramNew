package com.photogram.service;

import com.photogram.dao.PostDao;
import com.photogram.dao.UserDao;
import com.photogram.Exceptions.DaoException;
import com.photogram.dto.PostDto;
import com.photogram.mapper.PostMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostService implements PostServiceInterface<PostDto, Long> {

    public static final AtomicReference<PostService> INSTANCE = new AtomicReference<>();
    private static final PostDao postDao = PostDao.getInstance();
    public static final UserDao userDao = UserDao.getInstance();

    public static PostService getInstance() {
        if (INSTANCE.get() == null) {
            synchronized (PostService.class) {
                if (INSTANCE.get() == null) {
                    INSTANCE.set(new PostService());
                }
            }
        }
        return INSTANCE.get();
    }

    public List<PostDto> findAll() {
        return postDao.findAll().stream()
                .map(PostMapper::toPostDto)
                .toList();
    }

    public List<PostDto> findAllByUserId(Long userId) {
        return postDao.findById(userId).stream()
                .map(PostMapper::toPostDto).toList();
    }

    public PostDto findByUserId(Long userId) {
        return postDao.findById(userId).stream()
                .map(PostMapper::toPostDto).findAny().orElseThrow(() ->
                        new DaoException("The Post was not found while finding"));
    }

    public void create(PostDto postDto) {
        var user =
                userDao.findById(postDto.getUserId()).orElseThrow(() ->
                        new DaoException("User" + postDto.getUserId() + " not found while finding post"));
        var post = PostMapper.toPost(postDto, user);
        postDao.save(post);
    }

    public PostDto update(PostDto postDto) {
        return postDao.findById(postDto.getId())
                .map(post -> {
                    postDao.update(post);
                    return PostMapper.toPostDto(post);
                })
                .orElseThrow(() ->
                        new DaoException("Post" + postDto.getId() + " not found while updating the post"));
    }

    public void delete(Long postId) {
        postDao.delete(postId);
    }


}




