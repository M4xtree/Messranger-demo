package com.messranger.repositories;

import com.messranger.entity.Members;
import com.messranger.model.FilterColumn;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembersRepository extends BaseRepository<Members> {

    public MembersRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "members";
    }

    @Override
    protected String getIdColumn() {
        return "user_id" + "chat_id";
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"chat_id", "role", "can_delete_messages", "can_add_participants", "can_edit_messages", "caret", "joined_at"};
    }

    @Override
    protected String[] getColumnPlaceholders() {
        return new String[]{"?", "?", "?", "?", "?", "?", "?"};
    }

    @Override
    protected List<FilterColumn<?>> toFilterColumns(Members filter) {
        List<FilterColumn<?>> result = new ArrayList<>();
        Optional.ofNullable(filter.getChatId())
                .ifPresent(it -> result.add(new FilterColumn<>("chat_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        Optional.ofNullable(filter.getUserId())
                .ifPresent(it -> result.add(new FilterColumn<>("user_id", "=", it, (stmt, idx) -> stmt.setString(idx, it))));
        return result;
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Members member) throws SQLException {
        statement.setString(1, member.getChatId());
        statement.setString(2, member.getUserId());
        statement.setString(3, member.getRole());
        statement.setBoolean(4, member.isCanDeleteMessages());
        statement.setBoolean(5, member.isCanAddParticipants());
        statement.setBoolean(6, member.isCanEditMessages());
        statement.setString(7, member.getCaret());
        statement.setDate(8, Date.valueOf(member.getJoinedAt()));
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Members member) throws SQLException {
        statement.setString(1, member.getRole());
        statement.setBoolean(2, member.isCanDeleteMessages());
        statement.setBoolean(3, member.isCanAddParticipants());
        statement.setBoolean(4, member.isCanEditMessages());
        statement.setString(5, member.getCaret());
        statement.setString(6, member.getChatId());
        statement.setString(7, member.getUserId());
    }

    @Override
    protected Members mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Members(
                resultSet.getString("chat_id"),
                resultSet.getString("user_id"),
                resultSet.getString("role"),
                resultSet.getBoolean("can_delete_messages"),
                resultSet.getBoolean("can_add_participants"),
                resultSet.getBoolean("can_edit_messages"),
                resultSet.getString("caret"),
                resultSet.getDate("joined_at").toLocalDate()
        );
    }
}