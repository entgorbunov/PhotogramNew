package com.photogram.service.postService;

import com.photogram.dao.PostDao;
import com.photogram.dao.UserDao;
import com.photogram.dto.postDto.PostDtoFromDataBase;
import com.photogram.entity.Post;
import com.photogram.exceptions.DaoException;
import com.photogram.mapper.postMapper.PostMapperForDataBase;
import com.photogram.service.PostServiceInterface;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostServiceForDataBase implements PostServiceInterface<PostDtoFromDataBase, Long> {


    private static final AtomicReference<PostServiceForDataBase> POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE = new AtomicReference<>();



    private static final PostDao POST_DAO = PostDao.getPostDaoAtomicReference();
    private static final UserDao USER_DAO = UserDao.getUserDaoAtomicReference();
    private static final PostMapperForDataBase POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE = PostMapperForDataBase.getPostMapperForDataBaseAtomicReference();




    public static PostServiceForDataBase getPostServiceForDataBaseAtomicReference() {
        POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
        if (POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get() == null) {
            PostServiceForDataBase postServiceForDataBase = new PostServiceForDataBase();
            if (POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.compareAndSet(null, postServiceForDataBase)) {
                return postServiceForDataBase;
            } else {
                return POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
            }
        }
        return POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.get();
    }




    public List<PostDtoFromDataBase> findAll() {
        return POST_DAO.findAll().stream()
                .map(POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE::toDto)
                .toList();
    }

    @Override
    public PostDtoFromDataBase findById(Long postId) {
        return POST_DAO.findById(postId).stream()
                .map(POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE::toDto).findAny().orElseThrow(() ->
                        new DaoException("The Post was not found while finding"));
    }



    @Override
    public Long create(PostDtoFromDataBase postDtoFromDataBase) {
        var user =
                USER_DAO.findById(postDtoFromDataBase.getUserId()).orElseThrow(() ->
                        new DaoException("User" + postDtoFromDataBase.getUserId() + " not found while finding post"));
        var post = PostMapperForDataBase.toEntity(postDtoFromDataBase, user);
        POST_DAO.save(post);
        return post.getId();
    }

    @Override
    public PostDtoFromDataBase updateWithImage(PostDtoFromDataBase postDtoFromDataBase) {
        return POST_DAO.findById(postDtoFromDataBase.getUserId())
                .map(POST_DAO::update)
                .map(POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE::toDto)
                .orElseThrow(() ->
                        new DaoException("Post" + postDtoFromDataBase.getUserId() + " not found while updating the post")
                );
    }




    @Override
    public void delete(Long postId) {
        POST_DAO.delete(postId);
    }


    public List<PostDtoFromDataBase> findAllByUserId(Long userId) {
        List<PostDtoFromDataBase> listPostDtoFromDataBase = new ArrayList<>();
        List<Post> postDaoAllList = POST_DAO.findAllById(userId);
        for (Post post : postDaoAllList) {
            listPostDtoFromDataBase.add(POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(post));
        }
        return listPostDtoFromDataBase;
    }

    public List<PostDtoFromDataBase> findAllActivePosts() {
        List<PostDtoFromDataBase> listPostDtoFromDataBase = new ArrayList<>();
        List<Post> postDaoAllList = POST_DAO.findAllActive();
        for (Post post : postDaoAllList) {
            listPostDtoFromDataBase.add(POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(post));
        }
        return listPostDtoFromDataBase;
    }

    public List<PostDtoFromDataBase> findAllActivePostsByUserId(Long userId) {
        List<PostDtoFromDataBase> listPostDtoFromDataBase = new ArrayList<>();
        List<Post> postDaoAllList = POST_DAO.findAllActiveByUserId(userId);
        for (Post post : postDaoAllList) {
            listPostDtoFromDataBase.add(POST_MAPPER_FOR_DATA_BASE_ATOMIC_REFERENCE.toDto(post));
        }
        return listPostDtoFromDataBase;
    }




}




