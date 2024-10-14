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
        return new String[]{"creator_id", "parent_id", "pin_id", "chat", "name", "chat_type"};
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
        if (filter.getCreatorId() != null) {
            filterColumns.add("creator_id");
        }
        if (filter.getParentId() != null) {
            filterColumns.add("parent_id");
        }
        if (filter.getPinId() != null) {
            filterColumns.add("pin_id");
        }
        if (filter.getChat() != null) {
            filterColumns.add("chat");
        }
        if (filter.getName() != null) {
            filterColumns.add("name");
        }
        if (filter.getChatType() != null) {
            filterColumns.add("chat_type");
        }
        return filterColumns.toArray(new String[filterColumns.size()]);
    }

    @Override
    protected List<FilterColumn<?>> toFilterColumns(Chat filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getId())
                .ifPresent(it -> result.add(new FilterColumn<>("id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getCreatorId())
                .ifPresent(it -> result.add(new FilterColumn<>("creator_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getParentId())
                .ifPresent(it -> result.add(new FilterColumn<>("parent_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getPinId())
                .ifPresent(it -> result.add(new FilterColumn<>("pin_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getChat())
                .ifPresent(it -> result.add(new FilterColumn<>("chat", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getName())
                .ifPresent(it -> result.add(new FilterColumn<>("name", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getChatType())
                .ifPresent(it -> result.add(new FilterColumn<>("chat_type", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        return result;
    }

    @Override
    protected int prepareFilterStatement(PreparedStatement statement, Chat filter) throws SQLException {
        int index = 1;
        if (filter.getId() != null) {
            statement.setString(index++, filter.getId());
        }
        if (filter.getCreatorId()!= null) {
            statement.setString(index++, filter.getCreatorId());
        }
        if (filter.getParentId() != null) {
            statement.setString(index++, filter.getParentId());
        }
        if (filter.getPinId() != null) {
            statement.setString(index++, filter.getPinId());
        }
        if (filter.getChat()!= null) {
            statement.setString(index++, filter.getChat());
        }
        if (filter.getName() != null) {
            statement.setString(index++, filter.getName());
        }
        if (filter.getChatType() != null) {
            statement.setString(index++, filter.getChatType());
        }
        return index;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Chat user) throws SQLException {
        statement.setString(1, user.getId());
        statement.setString(2, user.getCreatorId());
        statement.setString(3, user.getParentId());
        statement.setString(4, user.getPinId());
        statement.setString(5, user.getChat());
        statement.setString(6, user.getName());
        statement.setString(7, user.getChatType());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Chat user) throws SQLException {
        statement.setString(1, user.getCreatorId());
        statement.setString(2, user.getParentId());
        statement.setString(3, user.getPinId());
        statement.setString(4, user.getChat());
        statement.setString(5, user.getName());
        statement.setString(6, user.getChatType());
        statement.setString(7, user.getId());
    }

    @Override
    protected Chat mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        String creatorId = resultSet.getString("creator_id");
        String parentId = resultSet.getString("parent_id");
        String pinId = resultSet.getString("pin_id");
        String chat = resultSet.getString("chat");
        String name = resultSet.getString("name");
        String chatType = resultSet.getString("chat_type");
        return new Chat(creatorId, parentId, pinId, chat, name, chatType);
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
