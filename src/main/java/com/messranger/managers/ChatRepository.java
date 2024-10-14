package com.messranger.managers;

import com.messranger.entity.Chat;
import com.messranger.entity.User;
import com.messranger.managers.model.FilterColumn;
import com.messranger.managers.model.PageRequest;
import com.zaxxer.hikari.HikariConfig;
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
    protected String[] getFilterColumns(Chat filter) {
        List<String> filterColumns = new ArrayList<>();
        if (filter.getId() != null) {
            filterColumns.add("id");
        }
        if (filter.getType() != null) {
            filterColumns.add("type");
        }
        if (filter.getCreatedBy() != null) {
            filterColumns.add("created_by");
        }
        if (filter.getName() != null) {
            filterColumns.add("name");
        }
        if (filter.getDescription() != null) {
            filterColumns.add("description");
        }
        return filterColumns.toArray(new String[0]);
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
    protected int prepareFilterStatement(PreparedStatement statement, Chat filter) throws SQLException {
        int index = 1;
        if (filter.getId() != null) {
            statement.setString(index++, filter.getId());
        }
        if (filter.getType() != null) {
            statement.setString(index++, filter.getType());
        }
        if (filter.getCreatedBy() != null) {
            statement.setString(index++, filter.getCreatedBy());
        }
        if (filter.getName() != null) {
            statement.setString(index++, filter.getName());
        }
        return index;
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

    @Override
    protected List<Chat> mapResultSetToEntities(ResultSet resultSet) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        while (resultSet.next()) {
            chats.add(mapResultSetToEntity(resultSet));
        }
        return chats;
    }
}