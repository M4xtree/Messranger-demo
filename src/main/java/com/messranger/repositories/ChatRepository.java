package com.messranger.repositories;

import com.messranger.entity.Chat;
import com.messranger.model.FilterColumn;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatRepository extends BaseRepository<Chat> {
    public ChatRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "chat";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"type", "created_by", "name", "description", "is_private", "created_at"};
    }

    @Override
    protected String[] getColumnPlaceholders() {
        return new String[]{"?", "?", "?", "?", "?", "?"};
    }

    @Override
    protected List<FilterColumn<?>> toFilterColumns(Chat filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getId())
                .ifPresent(it -> result.add(new FilterColumn<>("id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getType())
                .ifPresent(it -> result.add(new FilterColumn<>("type", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getCreatedBy())
                .filter(it -> it != null)
                .ifPresent(it -> result.add(new FilterColumn<>("created_by", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getName())
                .ifPresent(it -> result.add(new FilterColumn<>("name", "LIKE", "%" + it + "%", (stmt, idx) -> stmt.setString(idx, "%" + it + "%"))));
        return result;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Chat chat) throws SQLException {
        statement.setString(1, chat.getType());
        statement.setString(2, chat.getCreatedBy());
        statement.setString(3, chat.getName());
        statement.setString(4, chat.getDescription());
        statement.setBoolean(5, chat.isPrivate());
        statement.setTimestamp(6, Timestamp.valueOf(chat.getCreatedAt()));
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Chat chat) throws SQLException {
        statement.setString(1, chat.getType());
        statement.setString(2, chat.getCreatedBy());
        statement.setString(3, chat.getName());
        statement.setString(4, chat.getDescription());
        statement.setBoolean(5, chat.isPrivate());
        statement.setTimestamp(6, Timestamp.valueOf(chat.getCreatedAt()));
        statement.setString(7, chat.getId());
    }

    @Override
    protected Chat mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Chat(
                resultSet.getString("type"),
                resultSet.getString("created_by"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getBoolean("is_private"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}