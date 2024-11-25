package com.messranger.services;

import com.messranger.model.PageRequest;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
    T save(T instance);

    T update(T instance);

    Optional<T> find(String id);

    Optional<T> find(String firstId, String secondId);

    void delete(String id);

    List<T> findAll(PageRequest pageRequest);

    List<T> findAll(PageRequest pageRequest, T filter);

}
