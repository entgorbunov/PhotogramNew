package com.photogram.mapper;

public interface Mapper<F, T> {
    T toEntity(F dto);
    F toDto(T entity);
}
