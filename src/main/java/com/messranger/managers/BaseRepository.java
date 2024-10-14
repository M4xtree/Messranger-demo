package com.messranger.managers;

import com.messranger.entity.Identifier;
import com.messranger.managers.constants.SqlConstants;
import com.messranger.entity.User;
import com.messranger.managers.model.FilterColumn;
import com.messranger.managers.model.PageRequest;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseRepository<T> implements Repository<T> {

    private final HikariDataSource dataSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);

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
    protected abstract String[] getFilterColumns(T filter);

    protected String getInsertSql() {
        String columns = "id, " + String.join(SqlConstants.DELIMITER, getColumnNames());
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

    protected String getFindAllSql(PageRequest pageRequest, T filter) {
        String columns = "id, " + String.join(SqlConstants.DELIMITER, getColumnNames());
        StringBuilder baseSql = new StringBuilder(String.format(FIND_ALL_TEMPLATE, columns, getTableName()));

        String[] filterColumns = getFilterColumns(filter);
        if (filterColumns != null && filterColumns.length > 0) {
            baseSql.append(SqlConstants.WHERE);
            baseSql.append(String.join(" = ? " + SqlConstants.AND, filterColumns)).append(SqlConstants.DEFINE_DELIMITER);
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            baseSql.append(SqlConstants.ORDER_BY).append(String.join(SqlConstants.DELIMITER, pageRequest.getSortBy()));
        }

        baseSql.append(SqlConstants.PAGE_SIZE);
        return baseSql.toString();
    }

    protected abstract List<FilterColumn<?>> toFilterColumns(T filter);

    protected String getFindAllSqlNew(PageRequest pageRequest, List<FilterColumn<?>> filterColumns) {
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

    protected abstract void prepareInsertStatement(PreparedStatement statement, T instance) throws SQLException;
    protected abstract void prepareUpdateStatement(PreparedStatement statement, T instance) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
    protected abstract int prepareFilterStatement(PreparedStatement statement, T filter) throws SQLException;
    protected abstract List<T> mapResultSetToEntities(ResultSet resultSet) throws SQLException;


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
        return findAllNew(pageRequest, filter);
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement statement = connection.prepareStatement(getFindAllSql(pageRequest, filter))) {
//            int index = prepareFilterStatement(statement, filter);
//            statement.setInt(index++, pageRequest.getLimit());
//            statement.setLong(index++, pageRequest.getOffset());
//
//            ResultSet resultSet = statement.executeQuery();
//            return mapResultSetToEntities(resultSet);
//        } catch (SQLException e) {
//            LOGGER.error(e.toString());
//            throw new RuntimeException("Error while finding all instances", e);
//        }
    }

    public List<T> findAllNew(PageRequest pageRequest, T filter) {
        List<FilterColumn<?>> filterColumns = toFilterColumns(filter);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getFindAllSqlNew(pageRequest, filterColumns))) {
            Integer index = 1;
            for (FilterColumn<?> filterColumn: filterColumns) {
                filterColumn.getBindingFunction().accept(statement, index);
                index++;
            }
            statement.setInt(index++, pageRequest.getLimit());
            statement.setLong(index, pageRequest.getOffset());

            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToEntities(resultSet);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new RuntimeException("Error while finding all instances", e);
        }
    }
}























/*package com.messranger.managers;

import com.messranger.config.GlobalConfig;
import com.messranger.entity.Identifier;
import com.messranger.entity.User;
import com.messranger.managers.model.PageRequest;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseRepository<T extends Identifier> implements Repository<T>{

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);

    private static final String INSERT_TEMPLATE = SqlConstants.INSERT +  INTO %s (%s) VALUES (%s)";

    protected final HikariDataSource dataSource;

    private final String insertSql;

    public BaseRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;

        List<String> columnNames = getColumnNames();
        String insertColumnsSequence = String.join(SqlConstants.DELIMITER, columnNames);
        String bindings = columnNames.stream()
                .map(_ -> "?")
                .collect(Collectors.joining(SqlConstants.DELIMITER));
        this.insertSql = String.format(INSERT_TEMPLATE, getTableName(), insertColumnsSequence, bindings);
    }

    protected abstract String getSaveSql();
    protected abstract String getUpdateSql();
    protected abstract String getFindSql();
    protected abstract String getDeleteSql();
    protected abstract String getFindAllSql(PageRequest pageRequest);

    protected abstract void prepareSaveStatement(PreparedStatement statement, T instance) throws SQLException;
    protected abstract void prepareUpdateStatement(PreparedStatement statement, T instance) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract String getTableName();
    protected abstract List<String> getColumnNames();

    protected abstract List<T> mapResultSetToEntities(ResultSet resultSet) throws SQLException;

    @Override
    public T save(T instance) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getSaveSql())) {

            prepareSaveStatement(statement, instance);
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
             PreparedStatement statement = connection.prepareStatement(getUpdateSql())) {

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
             PreparedStatement statement = connection.prepareStatement(getFindSql())) {

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
             PreparedStatement statement = connection.prepareStatement(getDeleteSql())) {

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
}*/
