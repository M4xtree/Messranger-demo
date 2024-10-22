package com.messranger.managers.services;

import com.messranger.managers.model.PageRequest;
import com.messranger.managers.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public class BaseService<T> implements Service<T> {

    private final BaseRepository<T> repository;

    public BaseService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T instance) {
        return repository.save(instance);
    }

    @Override
    public T update(T instance) {
        return repository.update(instance);
    }

    @Override
    public Optional<T> find(String id) {
        return repository.find(id);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public List<T> findAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    public List<T> findAll(PageRequest pageRequest, T filter) {
        return repository.findAll(pageRequest, filter);
    }
}