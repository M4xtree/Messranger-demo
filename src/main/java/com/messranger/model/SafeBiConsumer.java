package com.messranger.model;

@FunctionalInterface
public interface SafeBiConsumer<T, U> {

    void accept(T first, U second) throws Exception;

}
