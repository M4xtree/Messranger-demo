package com.messranger.repositories;

import com.messranger.model.PageRequest;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    T save(T instance);

    T update(T instance);

    Optional<T> find(String id);

    void delete(String id);

    List<T> findAll(PageRequest pageRequest);

    List<T> findAll(PageRequest pageRequest, T filter);

}
