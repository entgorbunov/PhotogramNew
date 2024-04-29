package com.photogram.mapper;

public interface Mapper<F, T> {
    T mapFrom(F from);
}
