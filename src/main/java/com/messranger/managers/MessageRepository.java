package com.messranger.managers;

import com.messranger.entity.Message;
import com.messranger.managers.model.FilterColumn;
import com.messranger.managers.model.PageRequest;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageRepository extends BaseRepository<Message> {

    public MessageRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "messages";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"chat_id", "sender_id", "content", "created_at", "is_deleted", "is_read", "edited_at"};
    }

    @Override
    protected String[] getColumnPlaceholders() {
        return new String[]{"?", "?", "?", "?", "?", "?", "?"};
    }

    @Override
    protected String[] getFilterColumns(Message filter) {
        List<String> filterColumns = new ArrayList<>();
        if (filter.getChatId() != null) {
            filterColumns.add("chat_id");
        }
        if (filter.getSenderId() != null) {
            filterColumns.add("sender_id");
        }
        return filterColumns.toArray(new String[0]);
    }

    @Override
    protected List<FilterColumn<?>> toFilterColumns(Message filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getChatId())
                .ifPresent(it -> result.add(new FilterColumn<>("chat_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getSenderId())
                .ifPresent(it -> result.add(new FilterColumn<>("sender_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        return result;
    }

    @Override
    protected int prepareFilterStatement(PreparedStatement statement, Message filter) throws SQLException {
        int index = 1;
        if (filter.getChatId() != null) {
            statement.setString(index++, filter.getChatId());
        }
        if (filter.getSenderId() != null) {
            statement.setString(index++, filter.getSenderId());
        }
        return index;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Message message) throws SQLException {
        statement.setString(1, message.getId());
        statement.setString(2, message.getChatId());
        statement.setString(3, message.getSenderId());
        statement.setString(4, message.getContent());
        statement.setDate(5, Date.valueOf(message.getCreatedAt()));
        statement.setBoolean(6, message.isDeleted());
        statement.setBoolean(7, message.isRead());
        statement.setDate(8, message.getEditedAt() != null ? Date.valueOf(message.getEditedAt()) : null);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Message message) throws SQLException {
        statement.setString(1, message.getContent());
        statement.setBoolean(2, message.isDeleted());
        statement.setBoolean(3, message.isRead());
        statement.setDate(4, message.getEditedAt() != null ? Date.valueOf(message.getEditedAt()) : null);
        statement.setString(5, message.getId());
    }

    @Override
    protected Message mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Message(
                resultSet.getString("chat_id"),
                resultSet.getString("sender_id"),
                resultSet.getString("content"),
                resultSet.getDate("created_at").toLocalDate(),
                resultSet.getBoolean("is_deleted"),
                resultSet.getBoolean("is_read"),
                resultSet.getDate("edited_at") != null ? resultSet.getDate("edited_at").toLocalDate() : null
        );
    }

    @Override
    protected List<Message> mapResultSetToEntities(ResultSet resultSet) throws SQLException {
        List<Message> messages = new ArrayList<>();
        while (resultSet.next()) {
            messages.add(mapResultSetToEntity(resultSet));
        }
        return messages;
    }
}















