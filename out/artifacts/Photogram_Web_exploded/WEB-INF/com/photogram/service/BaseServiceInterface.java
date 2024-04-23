package com.photogram.service;


public interface BaseServiceInterface<E, K> {
    void delete(K id);

    void create(E entity);

    E update(E entity);

    E findById(K id);

}
