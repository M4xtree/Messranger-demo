package com.messranger.managers.services;

import com.messranger.managers.model.PageRequest;
import com.messranger.managers.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T> implements Service<T>{
    protected final BaseRepository<T> repository;

    public BaseService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public T create(T instance) {
        return repository.save(instance);
    }

    @Override
    public T update(String id, T instance) {
        Optional<T> existingInstance = repository.find(id);
        if (existingInstance.isPresent()) {
            return repository.update(instance);
        }
        throw new RuntimeException("Instance not found with id: " + id);
    }

    @Override
    public Optional<T> getById(String id) {
        return repository.find(id);
    }

    @Override
    public List<T> getAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    public void delete(String id) {
        Optional<T> existingInstance = repository.find(id);
        if (existingInstance.isPresent()) {
            repository.delete(id);
        } else {
            throw new RuntimeException("Instance not found with id: " + id);
        }
    }
}
