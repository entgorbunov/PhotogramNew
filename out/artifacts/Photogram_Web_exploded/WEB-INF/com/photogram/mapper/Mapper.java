package com.photogram.mapper;

public interface Mapper<F, T> {
    T toDto(F object);
    F toEntity(T object);

}
