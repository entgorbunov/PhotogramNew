package com.photogram.service;


import java.util.List;

public interface BaseServiceInterface<E, K> {
    void delete(K id);

    void create(E entity);

    E update(E entity);

    E findByUserId(K id);

    List<E> findAll();

}
