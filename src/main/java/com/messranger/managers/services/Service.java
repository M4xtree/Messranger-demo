package com.messranger.managers.services;

import com.messranger.managers.model.PageRequest;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
    T save(T instance);
    T update(T instance);
    Optional<T> find(String id);
    void delete(String id);
    List<T> findAll(PageRequest pageRequest);
    List<T> findAll(PageRequest pageRequest, T filter);
}
