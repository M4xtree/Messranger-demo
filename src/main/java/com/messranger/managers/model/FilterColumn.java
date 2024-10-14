package com.messranger.managers.model;

import java.sql.PreparedStatement;
import java.util.function.BiConsumer;

public class FilterColumn<T> {

    private final String columnName;

    private final String operator;

    private final T value;

    private final SafeBiConsumer<PreparedStatement, Integer> bindingFunction;

    public FilterColumn(String columnName, String operator, T value, SafeBiConsumer<PreparedStatement, Integer> bindingFunction) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
        this.bindingFunction = bindingFunction;
    }

    public String getColumnName() {
        return columnName;
    }

    public T getValue() {
        return value;
    }

    public SafeBiConsumer<PreparedStatement, Integer> getBindingFunction() {
        return bindingFunction;
    }

    @Override
    public String toString() {
        return columnName + " " + operator + " ?";
    }
}
