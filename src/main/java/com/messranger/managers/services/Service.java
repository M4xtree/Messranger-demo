package com.messranger.managers.services;

import com.messranger.managers.model.PageRequest;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
    T create(T instance);

    T update(String id, T instance);

    Optional<T> getById(String id);

    List<T> getAll(PageRequest pageRequest);

    void delete(String id);
}
