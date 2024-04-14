package com.photogram.daoInterfaces;

import com.photogram.entity.Post;

import java.sql.Connection;
import java.util.List;

public interface PostDaoInterface<P, L> extends BaseDaoInterface<P, L>{

    List<Post> findAllByUserId(Long userId, Connection connection);
}
