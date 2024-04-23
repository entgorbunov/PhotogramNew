package com.photogram.service;

import com.photogram.dto.UserDto;

import java.util.List;

public interface UserServiceInterface<U, L> extends BaseServiceInterface<U, L> {
    List<UserDto> findAll();
}
