package com.photogram.dao.likeDao;

public interface LikeDaoInterface<K, S> {
    void delete(K entityId, K userId, S entityType);
    void save(K userId, K entityId, S entityType);
    Boolean isLikedByUser(K userId, K entityId, S entityType);
    K countLikes(K entityId, S entityType);
}
