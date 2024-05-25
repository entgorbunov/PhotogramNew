package com.photogram.service;


import java.util.List;

public interface BaseServiceInterface<DTO, ID> {
    void delete(ID id);

    ID create(DTO dto);

    DTO updateWithImage(DTO dto);

    DTO findById(ID id);

    List<DTO> findAll();
}
