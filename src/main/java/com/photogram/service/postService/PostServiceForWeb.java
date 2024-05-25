package com.photogram.service.postService;

import com.photogram.dao.PostDao;
import com.photogram.dto.postDto.PostDtoFromWeb;
import com.photogram.entity.Post;
import com.photogram.exceptions.ServiceException;
import com.photogram.mapper.postMapper.PostMapperForWeb;
import com.photogram.service.ImageService;
import com.photogram.service.PostServiceInterface;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostServiceForWeb implements PostServiceInterface<PostDtoFromWeb, Long> {
    private static final String IMAGE_FOLDER = "posts/";
    private static final AtomicReference<PostServiceForWeb> POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE = new AtomicReference<>();
    private static final PostMapperForWeb POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE = PostMapperForWeb.getPostMapperForWebAtomicReference();
    private static final PostDao POST_DAO_ATOMIC_REFERENCE = PostDao.getPostDaoAtomicReference();
    private static final ImageService IMAGE_SERVICE_ATOMIC_REFERENCE = ImageService.getImageServiceAtomicReference();

    public static PostServiceForWeb getPostServiceForWebAtomicReference() {
        POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get();
        if (POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get() == null) {
            PostServiceForWeb postServiceForWeb = new PostServiceForWeb();
            if (POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE.compareAndSet(null, postServiceForWeb)) {
                return postServiceForWeb;
            } else {
                return POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get();
            }
        }
        return POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE.get();
    }






    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Long create(PostDtoFromWeb postDtoFromWeb) {
        try {
            if (postDtoFromWeb.getImage() != null && postDtoFromWeb.getImage().getSize() > 0) {
                IMAGE_SERVICE_ATOMIC_REFERENCE.upload(IMAGE_FOLDER + postDtoFromWeb.getImage().getSubmittedFileName(), postDtoFromWeb.getImage().getInputStream());
            }
            Post post = POST_MAPPER_FOR_WEB_ATOMIC_REFERENCE.toEntity(postDtoFromWeb);
            POST_DAO_ATOMIC_REFERENCE.save(post);

            return post.getId();
        } catch (IOException e) {
            throw new ServiceException("Error uploading the image", e);
        }
    }

    @Override
    public PostDtoFromWeb updateWithImage(PostDtoFromWeb postDtoFromWeb) {
        return null;
    }

    @Override
    public PostDtoFromWeb findById(Long aLong) {
        return null;
    }

    @Override
    public List<PostDtoFromWeb> findAll() {
        return List.of();
    }
}
