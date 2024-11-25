package com.messranger.repositories;

import com.messranger.entity.Message;
import com.messranger.model.FilterColumn;
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
    protected List<FilterColumn<?>> toFilterColumns(Message filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getChatId())
                .ifPresent(it -> result.add(new FilterColumn<>("chat_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getSenderId())
                .ifPresent(it -> result.add(new FilterColumn<>("sender_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        return result;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Message message) throws SQLException {
        statement.setString(1, message.getId());
        statement.setString(2, message.getChatId());
        statement.setString(3, message.getSenderId());
        statement.setString(4, message.getContent());
        statement.setTimestamp(5, Timestamp.valueOf(message.getCreatedAt()));
        statement.setBoolean(6, message.isDeleted());
        statement.setBoolean(7, message.isRead());
        statement.setTimestamp(8, message.getEditedAt() != null ? Timestamp.valueOf(message.getEditedAt()) : null);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Message message) throws SQLException {
        statement.setString(1, message.getChatId());
        statement.setString(2, message.getSenderId());
        statement.setString(3, message.getContent());
        statement.setTimestamp(4, Timestamp.valueOf(message.getCreatedAt()));
        statement.setBoolean(5, message.isDeleted());
        statement.setBoolean(6, message.isRead());
        statement.setTimestamp(7, message.getEditedAt() != null ? Timestamp.valueOf(message.getEditedAt()) : null);
        statement.setString(8, message.getId());
    }

    @Override
    protected Message mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
        Timestamp editedAtTimestamp = resultSet.getTimestamp("edited_at");

        return new Message(
                resultSet.getString("chat_id"),
                resultSet.getString("sender_id"),
                resultSet.getString("content"),
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null,
                resultSet.getBoolean("is_deleted"),
                resultSet.getBoolean("is_read"),
                editedAtTimestamp != null ? editedAtTimestamp.toLocalDateTime() : null
        );
    }
}















