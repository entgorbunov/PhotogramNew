package com.photogram.service;

import com.photogram.dto.PostDto;

import java.util.List;

public interface PostServiceInterface<P, L> extends BaseServiceInterface<P, L> {
    List<PostDto> findAll(Long userId);
}
