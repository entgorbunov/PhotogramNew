package com.photogram.dao.likeEntityDao;

public interface BaseDaoInterfaceForLikesEntity {
    Long countLikes(Long userId);
    void delete(Long id1, Long id2);
    void save(Long id1, Long id2);
    Boolean isLikedByUser(Long id1, Long id2);
}
