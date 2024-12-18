package com.messranger.repositories;

import com.messranger.constants.SqlConstants;
import com.messranger.entity.Members;
import com.messranger.model.FilterColumn;
import com.messranger.model.PageRequest;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T> implements Repository<T> {

    protected final HikariDataSource dataSource;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);

    private static final String INSERT_TEMPLATE = SqlConstants.INSERT +  SqlConstants.INTO + "%s (%s)" + SqlConstants.VALUES + "(%s)";
    private static final String UPDATE_TEMPLATE = SqlConstants.UPDATE + " %s" + SqlConstants.SET + "%s" + SqlConstants.WHERE + "%s = ?";
    private static final String SELECT_BY_ID_TEMPLATE = SqlConstants.SELECT + " %s " + SqlConstants.FROM + "%s" + SqlConstants.WHERE + "%s = ?";
    private static final String DELETE_TEMPLATE = SqlConstants.DELETE + SqlConstants.FROM + "%s" + SqlConstants.WHERE + "%s = ?";
    private static final String FIND_ALL_TEMPLATE = SqlConstants.SELECT + " %s" + SqlConstants.FROM + "%s";

    private String insertSql;
    private String updateSql;
    private String selectByIdSql;
    private String deleteSql;

    public BaseRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;

        insertSql = getInsertSql();
        updateSql = getUpdateSql();
        selectByIdSql = getSelectByIdSql();
        deleteSql = getDeleteSql();
    }

    protected abstract String getTableName();
    protected abstract String getIdColumn();
    protected abstract String[] getColumnNames();
    protected abstract String[] getColumnPlaceholders();

    protected abstract void prepareInsertStatement(PreparedStatement statement, T instance) throws SQLException;
    protected abstract void prepareUpdateStatement(PreparedStatement statement, T instance) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
    protected abstract List<FilterColumn<?>> toFilterColumns(T filter);

    @Override
    public T save(T instance) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            prepareInsertStatement(statement, instance);
            statement.executeUpdate();

            return instance;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while saving instance", e);
        }
    }

    @Override
    public T update(T instance) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSql)) {

            prepareUpdateStatement(statement, instance);
            statement.executeUpdate();

            return instance;
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while updating instance", e);
        }
    }

    @Override
    public Optional<T> find(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectByIdSql)) {

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while finding instance", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(String id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteSql)) {

            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while deleting instance", e);
        }
    }

    @Override
    public List<T> findAll(PageRequest pageRequest) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getFindAllSql(pageRequest))) {

            statement.setInt(1, pageRequest.getLimit());
            statement.setLong(2, pageRequest.getOffset());
            ResultSet resultSet = statement.executeQuery();

            return mapResultSetToEntities(resultSet);
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while finding all instances", e);
        }
    }
    @Override
    public List<T> findAll(PageRequest pageRequest, T filter) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getFindAllSql(pageRequest, filter))) {

            int index = 1;
            for (FilterColumn<?> filterColumn : toFilterColumns(filter)) {
                filterColumn.getBindingFunction().accept(statement, index++);
            }
            statement.setInt(index++, pageRequest.getLimit());
            statement.setLong(index, pageRequest.getOffset());

            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToEntities(resultSet);
        } catch (SQLException e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while finding all instances", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getInsertSql() {
        String columns = getIdColumn() + "," + String.join(SqlConstants.DELIMITER, getColumnNames());
        String placeholders = "?, "+String.join(SqlConstants.DELIMITER, getColumnPlaceholders());
        return String.format(INSERT_TEMPLATE, getTableName(), columns, placeholders);
    }

    protected String getUpdateSql() {
        String setClause = String.join(" = ?, ", getColumnNames()) + SqlConstants.DEFINE_DELIMITER;
        return String.format(UPDATE_TEMPLATE, getTableName(), setClause, getIdColumn());
    }

    protected String getSelectByIdSql() {
        String columns = String.join(SqlConstants.DELIMITER, getColumnNames());
        return String.format(SELECT_BY_ID_TEMPLATE, columns, getTableName(), getIdColumn());
    }

    protected String getDeleteSql() {
        return String.format(DELETE_TEMPLATE, getTableName(), getIdColumn());
    }

    protected String getFindAllSql(PageRequest pageRequest) {
        String columns = String.join(SqlConstants.DELIMITER, getColumnNames());
        String baseSql = String.format(FIND_ALL_TEMPLATE, columns, getTableName());

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            baseSql += SqlConstants.ORDER_BY + String.join(SqlConstants.DELIMITER, pageRequest.getSortBy());
        }

        baseSql += SqlConstants.PAGE_SIZE;
        return baseSql;
    }

    protected String getFindAllSql(PageRequest pageRequest, List<FilterColumn<?>> filterColumns) {
        String columns = "id, " + String.join(SqlConstants.DELIMITER, getColumnNames());
        StringBuilder baseSql = new StringBuilder(String.format(FIND_ALL_TEMPLATE, columns, getTableName()));
        if (null != filterColumns && !filterColumns.isEmpty()) {
            baseSql.append(SqlConstants.WHERE);
            List<String> predicates = filterColumns.stream()
                    .map(FilterColumn::toString)
                    .toList();
            baseSql.append(String.join(SqlConstants.AND, predicates));
        }
        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            baseSql.append(SqlConstants.ORDER_BY).append(String.join(SqlConstants.DELIMITER, pageRequest.getSortBy()));
        }
        baseSql.append(SqlConstants.PAGE_SIZE);
        return baseSql.toString();
    }

    protected String getFindAllSql(PageRequest pageRequest, T filter) {
        String columns = String.join(SqlConstants.DELIMITER, getColumnNames());
        StringBuilder baseSql = new StringBuilder(String.format(FIND_ALL_TEMPLATE, columns, getTableName()));
        if (filter != null) {
            List<FilterColumn<?>> filterColumns = toFilterColumns(filter);
            if (!filterColumns.isEmpty()) {
                baseSql.append(SqlConstants.WHERE);
                List<String> predicates = filterColumns.stream()
                        .map(FilterColumn::toString)
                        .toList();
                baseSql.append(String.join(SqlConstants.AND, predicates));
            }
        }
        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            baseSql.append(SqlConstants.ORDER_BY).append(String.join(SqlConstants.DELIMITER, pageRequest.getSortBy()));
        }
        baseSql.append(SqlConstants.PAGE_SIZE);
        return baseSql.toString();
    }


    protected List<T> mapResultSetToEntities(ResultSet resultSet) throws SQLException {
        List<T> entities = new ArrayList<>();
        while (resultSet.next()) {
            entities.add(mapResultSetToEntity(resultSet));
        }
        return entities;
    }

    public Optional<T> find(String firstId, String secondId) {
        String compKeySelectSql = SqlConstants.SELECT +  "*"+  SqlConstants.FROM + getTableName()
                + SqlConstants.WHERE + getIdColumn().split(",")[0] + " = ?" + SqlConstants.AND + getIdColumn().split(",")[1] + " = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(compKeySelectSql)) {
            statement.setString(1, firstId);
            statement.setString(2, secondId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding entity", e);
        }
        return Optional.empty();
    }

    public void delete(String firstId, String secondId) {
        String compKeyDeleteSql = SqlConstants.DELETE + SqlConstants.FROM + getTableName()
                + SqlConstants.WHERE + getIdColumn().split(",")[0] + " = ?" + SqlConstants.AND + getIdColumn().split(",")[1] + " = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(compKeyDeleteSql)) {
            statement.setString(1, firstId);
            statement.setString(2, secondId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting entity", e);
        }
    }
}