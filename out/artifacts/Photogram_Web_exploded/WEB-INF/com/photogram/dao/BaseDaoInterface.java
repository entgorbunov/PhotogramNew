package com.photogram.dao;

public interface BaseDaoInterface<E, K> {

    E update(E entity);

    void save(E e);

    void delete(K id);


}
