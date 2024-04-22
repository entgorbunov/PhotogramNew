package com.photogram.service;

import java.util.List;


public interface ServiceInterface<E, K> {
    List<E> findAllById(K id);
    E findAnyById(K id);
    E create(E entity);
    E update(E entity);
    boolean delete(K id);

}
