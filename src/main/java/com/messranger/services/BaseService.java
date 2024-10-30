package com.messranger.services;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.messranger.config.DataBaseConfig;
import com.messranger.model.PageRequest;
import com.messranger.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

public class BaseService<T> implements Service<T>{
    protected BaseRepository<T> repository;

    protected static final DataBaseConfig dbConfig = new DataBaseConfig();

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
        if(repository.find(id).isPresent())
            return repository.find(id);
        return Optional.empty();
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
        return repository.findAll(pageRequest,filter);
    }

}
