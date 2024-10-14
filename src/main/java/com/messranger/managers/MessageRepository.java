package com.messranger.managers;

import com.messranger.entity.Chat;
import com.messranger.entity.Message;
import com.messranger.managers.model.FilterColumn;
import com.messranger.managers.model.PageRequest;
import com.messranger.managers.model.SafeBiConsumer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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
        return new String[]{"chat_id", "message_text", "date", "time", "is_pinned"};
    }

    @Override
    protected String[] getColumnPlaceholders() {
        return new String[]{"?", "?", "?", "?", "?"};
    }

    @Override
    protected String[] getFilterColumns(Message filter) {
        List<String> filterColumns = new ArrayList<>();
        if (filter.getId() != null) {
            filterColumns.add("id");
        }
        if (filter.getChatId() != null) {
            filterColumns.add("chat_id");
        }
        if (filter.getMessageText() != null) {
            filterColumns.add("message_text");
        }
        if (filter.getDate() != null) {
            filterColumns.add("date");
        }
        if (filter.getDate() != null) {
            filterColumns.add("time");
        }
        filterColumns.add("is_pinned");

        return filterColumns.toArray(new String[filterColumns.size()]);
    }

    @Override
    protected List<FilterColumn<?>> toFilterColumns(Message filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getId())
                .ifPresent(it -> result.add(new FilterColumn<>("id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getChatId())
                .ifPresent(it -> result.add(new FilterColumn<>("chat_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getMessageText())
                .ifPresent(it -> result.add(new FilterColumn<>("message_text", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getDate())
                .ifPresent(it -> result.add(new FilterColumn<>("date", "=", it, (stmt, idx) -> stmt.setDate(idx, Date.valueOf(String.valueOf(it))))));
        Optional.ofNullable(filter.getTime())
                .ifPresent(it -> result.add(new FilterColumn<>("time", "=", it, (stmt, idx) -> stmt.setTime(idx, Time.valueOf(String.valueOf(it))))));
        Optional.of(filter.isPinned())
                .ifPresent(it -> result.add(new FilterColumn<>("is_pinned", "=", it, (stmt, idx) -> stmt.setBoolean(idx, it))));
        return result;
    }

    @Override
    protected int prepareFilterStatement(PreparedStatement statement, Message filter) throws SQLException {
        int index = 1;
        if (filter.getId() != null) {
            statement.setString(index++, filter.getId());
        }
        if (filter.getChatId() != null) {
            statement.setString(index++, filter.getChatId());
        }
        if (filter.getMessageText() != null) {
            statement.setString(index++, filter.getMessageText());
        }
        if (filter.getDate() != null) {
            statement.setDate(index++, Date.valueOf(String.valueOf(filter.getDate())));
        }
        if (filter.getDate() != null) {
            statement.setTime(index++, Time.valueOf(String.valueOf(filter.getTime())));
        }
        statement.setBoolean(index++, filter.isPinned());

        return index;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Message message) throws SQLException {
        statement.setString(1, message.getId());
        statement.setString(2, message.getChatId());
        statement.setString(3, message.getMessageText());
        statement.setDate(4, Date.valueOf(message.getDate()));
        statement.setTime(5, Time.valueOf(message.getTime()));
        statement.setBoolean(6, message.isPinned());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Message message) throws SQLException {
        statement.setString(1, message.getChatId());
        statement.setString(2, message.getMessageText());
        statement.setDate(3, Date.valueOf(message.getDate()));
        statement.setTime(4, Time.valueOf(message.getTime()));
        statement.setBoolean(5, message.isPinned());
        statement.setString(6, message.getId());
    }

    @Override
    protected Message mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        String chatId = resultSet.getString("chat_id");
        String messageText = resultSet.getString("message_text");
        Date date = resultSet.getDate("date");
        Time time = resultSet.getTime("time");
        Boolean isPinned = resultSet.getBoolean("is_pinned");
        return new Message(chatId, messageText, LocalDate.from(date.toInstant()), LocalTime.from(time.toInstant()),isPinned);
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

















