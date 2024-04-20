package com.photogram.dao;

import java.util.Optional;

public interface BaseDaoInterface<E, K> {

    void delete(K id);

    void save(E entity);

    void update(E entity);



}
